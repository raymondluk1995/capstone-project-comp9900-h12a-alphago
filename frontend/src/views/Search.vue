<template>
  <div id="search-page">
    <Header>
      <template v-if="this.hasLogin">
        <el-dropdown
          trigger="click"
          @command="handleCommand"
          style="align-items: center"
          placement="bottom"
        >
          <div class="user" @click="openValue">
            <el-avatar :size="70" :src="avatar"></el-avatar>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile" icon="el-icon-user-solid">
              My profile</el-dropdown-item
            >
            <el-dropdown-item command="property" icon="el-icon-house">
              My Properties</el-dropdown-item
            >
            <el-dropdown-item command="auction" icon="el-icon-s-home">
              My Auctions</el-dropdown-item
            >
            <el-dropdown-item command="notification" icon="el-icon-bell">
              Notifications</el-dropdown-item
            >
            <el-dropdown-item command="logout" icon="el-icon-turn-off">
              Log out</el-dropdown-item
            >
          </el-dropdown-menu>
        </el-dropdown>
      </template>
      <template v-else>
        <div class="back-btn">
          <span
            id="back-btn"
            style="padding: 2px 5px; font-size: 20px"
            @click="goto('login')"
            >Sign In <i class="el-icon-check"></i
          ></span>
          <div class="bottom-line"></div>
        </div>
        <div class="back-btn">
          <span
            id="back-btn2"
            style="padding: 2px 5px; font-size: 20px"
            @click="goto('register')"
            >Sign Up <i class="el-icon-user"></i
          ></span>
          <div class="bottom-line"></div>
        </div>
      </template>
    </Header>

    <div id="main" justify="center">
      <!-- Search Bar Part  -->
      <el-row
        type="flex"
        justify="center"
        class="search"
        style="margin-top: 10px"
      >
        <el-col :span="15">
          <div>
            <el-col :span="20">
              <vue-google-autocomplete
                ref="address"
                id="map"
                classname="form-control"
                placeholder="Please search suburb name or postcode here"
                v-on:placechanged="getAddressData"
                country="au"
                types="(cities)"
              >
              </vue-google-autocomplete>
            </el-col>

            <el-col style="margin-left: 2px" :span="2" id="search-btn">
              <el-button
                style="
                  height: 38px;
                  color: white;
                  background-color: rgb(20, 60, 127);
                "
                icon="el-icon-search"
                @click="toSearch"
              >
                Search
              </el-button>
            </el-col>
          </div>
        </el-col>
      </el-row>

      <!-- Filters Part  -->
      <el-row type="flex" justify="center" style="margin-top: 10px">
        <div id="filters">
          <el-row :gutter="3" type="flex" justify="center">
            <el-col :span="5">
              <h4 id="filter-title">FILTERS:</h4>
            </el-col>
            <el-col :span="5">
              <el-dropdown :hide-on-click="false" placement="bottom">
                <el-button
                  style="
                    color: white;
                    background-color: rgba(20, 60, 127, 0.9);
                    margin: 15px;
                    padding: 5px;
                    border-radius: 10px;
                  "
                >
                  <i class="el-icon-s-home" style="margin-right: 10px"></i
                  >{{ bedrooms }} Bedrooms<i
                    class="el-icon-arrow-down el-icon--right"
                  ></i>
                </el-button>
                <el-dropdown-menu slot="dropdown" style="margin: 0">
                  <el-dropdown-item>
                    <el-input-number
                      v-model="bedrooms"
                      @change="handleChange"
                      :min="1"
                      :max="10"
                    ></el-input-number>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-col>

            <el-col :span="5">
              <el-dropdown :hide-on-click="false" placement="bottom">
                <el-button
                  style="
                    color: white;
                    background-color: rgba(20, 60, 127, 0.9);
                    margin: 15px;
                    padding: 5px;
                    border-radius: 10px;
                  "
                >
                  <i class="el-icon-toilet-paper" style="margin-right: 10px"></i
                  >{{ bathrooms }} Bathrooms<i
                    class="el-icon-arrow-down el-icon--right"
                  ></i>
                </el-button>
                <el-dropdown-menu slot="dropdown" style="margin: 0">
                  <el-dropdown-item>
                    <el-input-number
                      v-model="bathrooms"
                      @change="handleChange"
                      :min="1"
                      :max="10"
                    ></el-input-number>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-col>

            <el-col :span="5">
              <el-dropdown :hide-on-click="false" placement="bottom">
                <el-button
                  style="
                    color: white;
                    background-color: rgba(20, 60, 127, 0.9);
                    margin: 15px;
                    padding: 5px;
                    border-radius: 10px;
                  "
                >
                  <i class="el-icon-truck" style="margin-right: 10px"></i
                  >{{ garages }} Garages<i
                    class="el-icon-arrow-down el-icon--right"
                  ></i>
                </el-button>
                <el-dropdown-menu slot="dropdown" style="margin: 0">
                  <el-dropdown-item>
                    <el-input-number
                      v-model="garages"
                      @change="handleChange"
                      :min="1"
                      :max="10"
                    ></el-input-number>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-col>

            <el-col :span="5">
              <el-dropdown :placement="bottom" @command="handleCommand_type">
                <el-button
                  style="
                    color: white;
                    background-color: rgba(20, 60, 127, 0.9);
                    margin: 15px;
                    padding: 5px;
                    border-radius: 10px;
                  "
                >
                  <i
                    class="el-icon-collection-tag"
                    style="margin-right: 10px"
                  ></i
                  >Type: {{ type
                  }}<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="All"> All </el-dropdown-item>
                  <el-dropdown-item command="Apartment">
                    Apartment
                  </el-dropdown-item>
                  <el-dropdown-item command="Unit"> Unit </el-dropdown-item>
                  <el-dropdown-item command="House"> House </el-dropdown-item>
                  <el-dropdown-item command="Studio"> Studio </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-col>

            <el-col :span="5">
              <el-dropdown :hide-on-click="false" placement="bottom">
                <el-button
                  style="
                    color: white;
                    background-color: rgba(20, 60, 127, 0.9);
                    margin: 15px;
                    padding: 5px;
                    border-radius: 10px;
                  "
                >
                  <i class="el-icon-coin" style="margin-right: 10px"></i>Price<i
                    class="el-icon-arrow-down el-icon--right"
                  ></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>
                    <div>min</div>
                    <el-input-number
                      v-model="minPrice"
                      @change="handleChange"
                      :min="0"
                      :max="10000"
                      size="small"
                    ></el-input-number>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div>max</div>
                    <el-input-number
                      v-model="maxPrice"
                      @change="handleChange"
                      :min="0"
                      :max="10000"
                      size="small"
                    ></el-input-number>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-col>

            <el-col :span="6">
              <el-dropdown
                :hide-on-click="false"
                placement="bottom"
                @command="handleCommand2"
              >
                <el-button
                  style="
                    color: white;
                    background-color: rgba(20, 60, 127, 0.9);
                    margin: 15px;
                    padding: 5px;
                    border-radius: 10px;
                  "
                >
                  <i class="el-icon-sort" style="margin-right: 10px"></i>Sort<i
                    class="el-icon-arrow-down el-icon--right"
                  ></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="0"> Recommended </el-dropdown-item>
                  <el-dropdown-item command="+">
                    Price: Low to High
                  </el-dropdown-item>
                  <el-dropdown-item command="-">
                    Price: High to Low
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-col>

            <el-col :span="5">
              <el-button
                  style="
                    color: white;
                    background-color: rgb(35, 150, 19);
                    margin: 15px;
                    padding: 5px 10px;
                    border-radius: 10px;
                  "
                ><i class="el-icon-check" style="margin-right: 10px"></i>Apply
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-row>
    </div>
  </div>
</template>



<script>
import Header from "@/components/Header.vue";
import { mapActions } from "vuex";
import $ from "jquery";
import VueGoogleAutocomplete from "vue-google-autocomplete";

export default {
  name: "Search",
  title: "Find A Property",
  components: {
    Header,
    VueGoogleAutocomplete,
  },

  data() {
    return {
      isEmpty: false,
      hasLogin: false,
      searchPropertyList: [],
      filterPropertyList: [],
      minPrice: 0,
      maxPrice: 999_999_999,
      bedrooms: 1,
      bathrooms: 1,
      garages: 1,
      order: "",
      total: 0,
      pageSize: 8,
      currentPage: 1,
      search: "",
      type: "All",
    };
  },

  created() {
    this.username = localStorage.getItem("username");
    // this.username = this.$store.state.username;
    if (this.username !== null) {
      this.hasLogin = true;
      this.avatar = localStorage.getItem("avatar");
    }
    this.firstname = localStorage.getItem("firstname");
  },

  mounted() {
    $("#back-btn").hover(function (event) {
      $(this).stop().animate({ "margin-left": "10px" }, 300);
      $(this).next(".bottom-line").stop().animate({ width: "100px" }, 300);
    });
    $("#back-btn").mouseleave(function (event) {
      $(this).stop().animate({ "margin-left": "0" }, 300);
      $(this).next(".bottom-line").stop().animate({ width: "0" }, 300);
    });

    $("#back-btn2").hover(function (event) {
      $(this).stop().animate({ "margin-left": "10px" }, 300);
      $(this).next(".bottom-line").stop().animate({ width: "100px" }, 300);
    });
    $("#back-btn2").mouseleave(function (event) {
      $(this).stop().animate({ "margin-left": "0" }, 300);
      $(this).next(".bottom-line").stop().animate({ width: "0" }, 300);
    });
  },

  methods: {
    ...mapActions(["logout"]),

    openValue() {
      this.show = !this.show;
    },
    getvalue(index, item) {
      this.value = item.name;
      this.show = false;
    },

    handleCommand(command) {
      switch (command) {
        case "profile":
          this.$router.push("/profile");
          break;
        case "auction":
          this.$router.push("/auctionmag");
          break;
        case "property":
          this.$router.push("/propmag");
          break;
        case "notification":
          break;
        case "logout":
          this.$axios.post("/user/logout").then((response) => {
            if (response.status >= 200 && response.status < 300) {
              if (response.data.code === 200) {
                this.logout();
                this.$router.replace("/");
              } else {
                console.log(response.msg);
              }
            } else {
              console.log(response.msg);
            }
          });
          break;
        default:
          break;
      }
    },

    handleCommand_type(command) {
      this.type = command;
    },

    handleCommand2(command) {
      if (command === "+") {
        this.order = "price";
      }
      if (command === "-") {
        this.order = "-price";
      }
      if (command === "0") {
        this.order = "";
      }
    },

    goto(name) {
      console.log(name);
      this.$router.push({ name: name });
    },
    back() {
      this.$router.go(-1);
    },

    goDetails(item) {
      this.$router.push({
        path: "/auction",
        query: {
          id: item.aid,
        },
      });
    },

    showTime(time) {
      // let time = dayjs(day).format("YYYY-MM-DD HH:mm:ss");
      let MONTH = [
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec",
      ];
      let day = dayjs(time).day();
      let mon = MONTH[dayjs(time).month()];
      let year = dayjs(time).year();
      let hour = dayjs(time).hour();
      let min = dayjs(time).minute();
      let formatTime = `${day} ${mon} ${year}, ${hour}:${min}`;
      return formatTime;
    },
  },
};
</script>





<style scoped lang="scss">
@import url("https://fonts.googleapis.com/css2?family=Dancing+Script:wght@700&display=swap");
.user {
  display: flex;
  align-items: center;
  p {
    margin-left: 10px;
  }
}

.back-btn {
  cursor: pointer;
  position: relative;
  /*border:1px solid #123123;*/
}
.back-btn .bottom-line {
  position: absolute;
  /*margin-left:40px;*/
  width: 0;
  height: 2px;
  left: 0;
  bottom: -1px;
  background-color: #3b4c73;
}

#filters {
  margin: 0;
  padding: 0;
  background-color: rgba(20, 150, 251, 0.5);
  width: auto;
  border-radius: 20px;
}

#filter-title {
  margin: 13px;
  margin-left: 15px;
  padding: 0;
  color: white;
  font-family: "Dancing Script", cursive;
}
</style>