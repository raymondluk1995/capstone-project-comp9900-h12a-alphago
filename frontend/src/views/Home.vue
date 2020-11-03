<template>
  <div class="home">
    <Header>
      <template v-if="this.hasLogin">
        <el-dropdown trigger="hover" @command="handleCommand" style="align-items: center" placement="bottom">
          <div class="user"  @click="openValue">
              <el-badge v-show="parseInt(this.unread) !== 0" :value="this.unread" :max="99" class="item">
            <el-avatar :size="70" :src="avatar"></el-avatar>
              </el-badge>
          </div>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="profile" icon="el-icon-user-solid"> My profile</el-dropdown-item>
                <el-dropdown-item command="property"  icon="el-icon-house"> My Properties</el-dropdown-item>
                <el-dropdown-item command="auction" icon="el-icon-s-home"> My Auctions</el-dropdown-item>

                <el-dropdown-item command="notification"  icon="el-icon-bell">
                    Notifications <el-badge v-show="parseInt(this.unread) !== 0" class="mark" :value="this.unread" style="padding:0;background-color: white"/>
                </el-dropdown-item>

                <el-dropdown-item command="logout" icon="el-icon-turn-off"> Log out</el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
      </template>
      <template v-else >

        <div class="back-btn">
          <span  id="back-btn" style="padding:2px 5px;font-size:20px;" @click="goto('login')">Sign In <i class="el-icon-check"></i></span>
          <div class="bottom-line"></div>
        </div>
        <div class="back-btn">
          <span  id="back-btn2" style="padding:2px 5px;font-size:20px;" @click="goto('register')">Sign Up <i class="el-icon-user"></i></span>
          <div class="bottom-line"></div>
        </div>

      </template>
    </Header>
    <div class="main" id="main">
<!--    <el-row type="flex" justify="center" style="margin: 0">-->
<!--      <el-col :span="12">-->
<!--        <el-row type="flex" justify="space-between" class="search-criteria">-->
<!--          <el-col :span="6">-->
<!--            <img src="@/assets/bath.png" alt="" />-->
<!--            <el-select v-model="bathNum" placeholder="Select">-->
<!--              <el-option-->
<!--                      v-for="item in nums"-->
<!--                      :key="item.value"-->
<!--                      :label="item.label"-->
<!--                      :value="item.value">-->
<!--              </el-option>-->
<!--            </el-select>-->
<!--          </el-col>-->

<!--          <el-col :span="6">-->
<!--            <img src="@/assets/bed.png" alt="" />-->
<!--            <el-select v-model="bedroomNum" placeholder="Select">-->
<!--              <el-option-->
<!--                      v-for="item in nums"-->
<!--                      :key="item.value"-->
<!--                      :label="item.label"-->
<!--                      :value="item.value">-->
<!--              </el-option>-->
<!--            </el-select>-->
<!--          </el-col>-->

<!--          <el-col :span="6">-->
<!--            <img src="@/assets/parking.png" alt="" />-->
<!--            <el-select v-model="carNum" placeholder="Select">-->
<!--              <el-option-->
<!--                      v-for="item in nums"-->
<!--                      :key="item.value"-->
<!--                      :label="item.label"-->
<!--                      :value="item.value">-->
<!--              </el-option>-->
<!--            </el-select>-->
<!--          </el-col>-->

<!--&lt;!&ndash;          <el-col :span="3">&ndash;&gt;-->
<!--&lt;!&ndash;            <el-select v-model="value" placeholder="Select">&ndash;&gt;-->
<!--&lt;!&ndash;              <el-option&ndash;&gt;-->
<!--&lt;!&ndash;                      v-for="item in options"&ndash;&gt;-->
<!--&lt;!&ndash;                      :key="item.value"&ndash;&gt;-->
<!--&lt;!&ndash;                      :label="item.label"&ndash;&gt;-->
<!--&lt;!&ndash;                      :value="item.value">&ndash;&gt;-->
<!--&lt;!&ndash;              </el-option>&ndash;&gt;-->
<!--&lt;!&ndash;            </el-select>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-col>&ndash;&gt;-->
<!--        </el-row>-->
<!--      </el-col>-->
<!--    </el-row>-->

    <el-row type="flex" justify="center" class="search">
      <el-col :span="15">
        <el-input
                class="input"
                placeholder="Search by address/suburb/others"
                prefix-icon="el-icon-search"
                @keyup.enter.native="toSearch"
                v-model="serachKey"
        >
<!--          <el-button-->
<!--                  slot="append"-->
<!--                  icon="el-icon-search"-->
<!--                  @click="toSearch"-->
<!--          ></el-button>-->
        </el-input></el-col>
    </el-row>
       <el-row type="flex" justify="center" >
           <el-col :span="6" >

           </el-col>
           <el-col :span="6">

           </el-col>
           <el-col :span="6">

           </el-col>
       </el-row>

    </div>
    <Footer></Footer>
  </div>
</template>

<script>
// @ is an alias to /src
import Header from '@/components/Header.vue'
import { mapActions } from "vuex";
import $ from 'jquery'
import Canvas from '@/utils/canvas-nest.min.js'

export default {
  name: 'Home',
  title: 'AlphaGo Home',
  components: {
    Header,
    // Canvas
  },
    data() {
      return {
          hasLogin: false,
          bathNum: 1,
          bedroomNum: 1,
          carNum: 1,
          serachKey: "",
          unread:'',

        // 浏览器宽度
        screenWidth :0,
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
          }
        ],

      }
    },
    created () {
        this.username = localStorage.getItem('username');
        // this.username = this.$store.state.username;
        if(this.username!==null){
            this.hasLogin = true;
            this.avatar = localStorage.getItem('avatar');
            this.$axios
                .get('/notification/unread')
                .then(response => {
                    if (response.data.code === 200) {
                        this.unread = response.data.result;
                    }
                })
                .catch(function (error) {
                    this.$message.error(error);
                });
        }
        this.firstname=  localStorage.getItem('firstname');



    },

    methods: {
      ...mapActions(["logout"]),

        openValue(){
            this.show=!this.show;
        },
        getvalue(index,item){
            this.value=item.name;
            this.show=false;
        },


      handleCommand(command) {
        switch (command) {
          case "profile":
            this.$router.push("/profile");
            break;
          case "property":
            this.$router.push("/propmag");
            break;
          case "auction":
            this.$router.push("/auctionmag");
            break;
          case "notification":
              this.$router.push("/notice");
            break;
          case "logout":
            this.$axios.post('/user/logout')
                    .then((response) => {
                      if (response.status >= 200 && response.status < 300){
                        if (response.data.code === 200){
                          this.logout();
                          location.reload()
                        }else{
                          console.log(response.msg)
                        }
                      }else{
                        console.log(response.msg)
                      }
                    })
            break;
          default:
            break;
        }
      },
      toSearch() {
        console.log('search')
      },
      goto(name) {
        this.$router.push({name: name});
      },
    },

    mounted() {
      $("#back-btn").hover(function(event) {
        $(this).stop().animate({"margin-left": "10px"}, 300);
        $(this).next(".bottom-line").stop().animate({"width": "100px"}, 300);
      });
      $("#back-btn").mouseleave(function(event) {
        $(this).stop().animate({"margin-left": "0"}, 300);
        $(this).next(".bottom-line").stop().animate({"width": "0"}, 300);
      });

      $("#back-btn2").hover(function(event) {
        $(this).stop().animate({"margin-left": "10px"}, 300);
        $(this).next(".bottom-line").stop().animate({"width": "100px"}, 300);
      });
      $("#back-btn2").mouseleave(function(event) {
        $(this).stop().animate({"margin-left": "0"}, 300);
        $(this).next(".bottom-line").stop().animate({"width": "0"}, 300);
      });

        $("#main").hover(function(event) {
            $(this).stop().animate({"opacity": "0.9"}, 300);
        });
        $("#main").mouseleave(function(event) {
            $(this).stop().animate({"opacity": "0.8"}, 300);
        });
    }
  }
</script>

<style lang="scss">
.user {
  display: flex;
  align-items: center;
}
.main{
    height:600px;
    opacity: 0.8;
    z-index:-3;
  /*box-shadow: inset 0 2px 10px 4px #1a1b1d;*/
  background-image: url("../assets/main-bg-3.png");
}

.el-input--prefix .el-input__inner {
  border-radius: 50px !important;
  height:50px;
  font-size:15px;
}

.search{
  .input{
    margin: 20% auto;
  }
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

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

.back-btn{
  cursor: pointer;
  position:relative;
  width:150px;
  /*border:1px solid #123123;*/
}
.back-btn .bottom-line{
  position: absolute;
  /*margin-left:40px;*/
  width: 0;
  height: 2px;
  left:0;
  bottom: -1px;
  background-color: #3b4c73;
}
.el-dropdown-menu{
    padding: 25px 20px 25px 10px !important;
    border-radius: 0 !important;
}

.el-dropdown-menu__item{
        &:hover{
            transform:translateX(10px);
            transition-duration: 0.5s;
            border-bottom: 2px solid rgba(4, 10, 30, 0.96);
            background-color: white !important;
            color: #143469 !important;
        }
}

</style>
