// Add the following code on the line 442 in the "index.js"
// file in vue-better-google-places-autocomplete package
// style:{
//     listStyleType:"none"
//                   },

<template>
  <div class="home">
    <Header>
      <template v-if="this.hasLogin">
        <el-dropdown
          trigger="click"
          @command="handleCommand"
          style="align-items: center"
          placement="bottom"
        >
          <div class="user">
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
        <el-button round @click="goto('login')">Sign In</el-button>
        <el-button round type="primary" @click="goto('register')"
          >Sign Up</el-button
        >
      </template>
    </Header>

    <el-carousel :interval="5000" type="card" height="1000">
      <el-carousel-item
        v-for="item in bigScreen.images"
        :key="item.name"
        :name="item.name"
      >
        <img style="width: 100%; height: 100%" :src="item.url" />
      </el-carousel-item>
    </el-carousel>

    <el-row type="flex" justify="center" style="margin: 40px 0">
      <el-col :span="12">
        <el-row type="flex" justify="space-between" class="search-criteria">
          <el-col :span="6">
            <img src="@/assets/bath.png" alt="" />
            <el-select v-model="bathNum" placeholder="Select">
              <el-option
                v-for="item in nums"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-col>

          <el-col :span="6">
            <img src="@/assets/bed.png" alt="" />
            <el-select v-model="bedroomNum" placeholder="Select">
              <el-option
                v-for="item in nums"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-col>

          <el-col :span="6">
            <img src="@/assets/parking.png" alt="" />
            <el-select v-model="carNum" placeholder="Select">
              <el-option
                v-for="item in nums"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-col>

          <!--          <el-col :span="3">-->
          <!--            <el-select v-model="value" placeholder="Select">-->
          <!--              <el-option-->
          <!--                      v-for="item in options"-->
          <!--                      :key="item.value"-->
          <!--                      :label="item.label"-->
          <!--                      :value="item.value">-->
          <!--              </el-option>-->
          <!--            </el-select>-->
          <!--          </el-col>-->
        </el-row>
      </el-col>
    </el-row>

    <el-row type="flex" justify="center" style="margin: 40px 0">
      <el-col :span="15">
        <!-- <el-input
                class="input"
                placeholder="Search by address/suburb/others"
                prefix-icon="el-icon-search"
                v-model="serachKey"
        > -->
        <div style="display: inline-block; width: 100%">
          <vue-google-autocomplete
            ref="address"
            id="map"
            classname="form-control"
            placeholder="Please search postcode or suburb name"
            v-on:placechanged="getAddressData"
            country="au"
            types="(cities)"
          >
          </vue-google-autocomplete>
        </div>
        <!-- </el-input> -->
      </el-col>

      <el-col :span="1">
        <el-button style="height: 38px" icon="el-icon-search" @click="toSearch">
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import VueGoogleAutocomplete from "vue-google-autocomplete";
// @ is an alias to /src
import Header from "@/components/Header.vue";
import { mapActions } from "vuex";

export default {
  name: "Home",
  title: "AlphaGo Home",
  components: {
    Header,
    VueGoogleAutocomplete 
  },
  data() {
    return {
      address:'',
      // address has the following properties:
      // street_number, route, locality, administrative_area_level_1, country, postal_code, latitude, longitude.
      hasLogin: false,
      bathNum: 1,
      bedroomNum: 1,
      carNum: 1,
      serachKey: "",
      bigScreen: {
        images: [
          {
            name: "bigScreen01",
            url:
              "https://www.armourcomms.com/wp-content/uploads/2017/10/company-main-banner.jpg",
          },
          {
            name: "bigScreen02",
            url:
              "https://www.centurybizsolutions.net/wp-content/uploads/2016/01/Banner-Buildings-1.png",
          },
          {
            name: "bigScreen03",
            url:
              "http://cdn26.us1.fansshare.com/photo/buildingconstruction/banner-building-construction-1958425491.jpg",
          },
        ],
      },
      // 图片父容器高度
      bannerHeight: 1000,
      // 浏览器宽度
      screenWidth: 0,

      nums: [
        {
          value: 1,
          label: 1,
        },
        {
          value: 2,
          label: 2,
        },
        {
          value: 3,
          label: 3,
        },
        {
          value: 4,
          label: 4,
        },
        {
          value: 5,
          label: 5,
        },
      ],
      // options: [
      //   {
      //     value: 1,
      //     label: "others",
      //   },
      // ]
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
  // computed: {
  //   ...mapState(["firstname"]),
  // },
  methods: {
    ...mapActions(["logout"]),
    handleCommand(command) {
      switch (command) {
        case "profile":
          this.$router.push("/profile");
          break;
        case "property":
          this.$router.push("/propmag");
          break;
        case "auction":
          this.$router.push("/auction");
          break;
        case "notification":
          break;
        case "logout":
          this.$axios.post("/user/logout").then((response) => {
            if (response.status >= 200 && response.status < 300) {
              if (response.data.code === 200) {
                this.logout();
                location.reload();
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
    getAddressData: function (addressData, placeResultData, id) {
      this.address = addressData;
    },
    toSearch() {},
    goto(name) {
      this.$router.push({ name: name });
    },
    setSize: function () {
      // 通过浏览器宽度(图片宽度)计算高度
      this.bannerHeight = (400 / 1920) * this.screenWidth;
    },
  },
  mounted() {
    // 首次加载时,需要调用一次
    this.screenWidth = window.innerWidth;
    this.setSize();
    // 窗口大小发生改变时,调用一次
    window.onresize = () => {
      this.screenWidth = window.innerWidth;
      this.setSize();
    };
    this.$refs.address.focus();
  },
};
</script>

<style scoped lang="scss">
.user {
  display: flex;
  align-items: center;
}

.el-input--prefix .el-input__inner {
  border-radius: 20px !important;
}
.search-criteria {
  .el-col {
    display: flex;
    align-items: center;
  }
  img {
    width: 35px;
    height: 35px;
    margin-right: 10px;
  }
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.btn-default:hover {
  background-color: rgba(69, 95, 147, 0.8);
}

.search-span {
  width: 100%;
  background-color: rgba(200, 213, 249, 0.4);
}

span {
  border: 1px solid rgba(138, 138, 138, 0.4);
}

li {
  list-style: none;
  list-style-type: none;
  display: inline-block;
}
</style>
