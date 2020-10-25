<template>
  <div class="login">
    <Header>
      <el-button  type="" @click="back">Back</el-button>
    </Header>
    <el-row type="flex" justify="center" >
      <el-col :span="15" >
        <div class="img-size" :style={height:vdaH}>
        <img src="../assets/bg2-logo.png" alt="" >
        </div>
      </el-col>

      <el-col :span="9">

<!--          <el-form-item v-if="loginByuser"  label="Username:" prop="username">-->
<!--          <el-input v-model="form.username"></el-input>-->
<!--        </el-form-item>-->

<!--          <el-form-item v-else label="Email:" prop="email">-->
<!--            <el-input v-model="form.email"></el-input>-->
<!--          </el-form-item>-->

<!--          <el-form-item label="Password:" prop="password">-->
<!--            <el-input-->
<!--                    type="password"-->
<!--                    v-model="form.password"-->
<!--                    show-password-->
<!--            ></el-input>-->
<!--          </el-form-item>-->
<!--        </el-form>-->


        <div id="container">
<!--          <el-button :type="byuserType" icon="el-icon-user-solid" circle @click="byuser"></el-button>-->
<!--          <el-button :type="byemailType" icon="el-icon-message" circle @click="byemail"></el-button>-->
          <div class="btn-style" >

<!--            <transition name="show">-->
           <span v-if="!loginByuser" id="btn-move1" @click="byuser">Login By Username<i class="el-icon-right el-icon--right"></i></span>
            <span v-else  id="btn-move2" @click="byemail">Login By Email<i class="el-icon-right el-icon--right"></i></span>
            <div class="bottom-line"></div>

<!--            </transition>-->


<!--            <transition name="show">-->
<!--              <span v-if="!loginByuser" id="btn-move1" @click="byuser">Login By Username<i class="el-icon-right el-icon&#45;&#45;right"  ></i></span>-->
<!--               <span v-if="loginByuser" id="btn-move2" @click="byemail">Login By Email<i class="el-icon-right el-icon&#45;&#45;right"></i> </span>-->

<!--            </transition>-->

          </div>


          <div class="content" >
            <div class="details">
              <h1 class="title">Sign In</h1>



              <div class="item">
                <transition name="show">-->
                <label v-show="loginByuser" for="item-firsthev1">Username</label>
                </transition>
                <transition name="show">-->
                <label v-show="!loginByuser" for="item-firsthev2">Email</label>
                </transition>

                <input  v-if="loginByuser" v-model="form.username" id="item-firsthev1" type="text" >
                <input  v-else v-model="form.email" id="item-firsthev2" type="text" >
                <div class="bottom-line"></div>
              </div>
<!--              <div class="item" v-else>-->
<!--                <label for="item-firsthev2">Email</label>-->
<!--                <input v-model="form.email" id="item-firsthev2" type="text" >-->
<!--                <div class="bottom-line"></div>-->
<!--              </div>-->

              <div class="item">
                <label for="next-hev">Password</label>
                <input v-model="form.password" id="next-hev" type="password">
                <div class="bottom-line"></div>
              </div >

            <div class="item" style="margin-top:40px">
              <el-button class="btn" @click="goto('register')">Sign Up</el-button>
              <el-button type="info" plane @click="forgetpwd">Forget Password?</el-button>
              <el-button v-if="loginByuser" type="primary" @click="signInUser" style="float: right;">Sign in</el-button>
              <el-button v-else type="primary" @click="signInEmail" style="float: right;">Sign in</el-button>
            </div>


            </div>
          </div>
        </div>

      </el-col>
    </el-row>
  </div>
</template>

<script>
  import Header from "@/components/Header.vue";
  import { mapMutations } from "vuex";
  import $ from 'jquery'

  export default {
    title: 'Login',
    components: {
      Header,
    },

    created () {
      this.username = localStorage.getItem('username');
      if (this.username !== null) {
        this.$message.error("You have already signed in!");
        this.$router.push("/");
      }
        let h = document.documentElement.clientHeight  || document.body.clientHeight;
        this.vdaH = h - 120 + 'px';
    },
    mounted(){


      $("#btn-move1").hover(function(event) {
        $(this).stop().animate({"margin-left": "10px"}, 300);
        $(this).next(".bottom-line").stop().animate({"width": "150px"}, 300);
      });

      $("#btn-move2").hover(function(event) {
        $(this).stop().animate({"margin-left": "10px"}, 300);
        $(this).next(".bottom-line").stop().animate({"width": "150px"}, 300);
      });

      $("#btn-move1").mouseleave(function(event) {
        $(this).stop().animate({"margin-left": "0"}, 300);
        $(this).next(".bottom-line").stop().animate({"width": "0"}, 300);
      });

      $("#btn-move2").mouseleave(function(event) {
        $(this).stop().animate({"margin-left": "0"}, 300);
        $(this).next(".bottom-line").stop().animate({"width": "0"}, 300);
      });

        $("input").focus(function(event) {
          //label动态上升，升至顶部
          $(this).siblings("label").stop().animate({"bottom": "50px"}, 300);
          //div模拟的下边框伸出，其width动态改变至input的width
          $(this).next(".bottom-line").stop().animate({"width": "400px"}, 300);
        });

        // 输入框失去焦点时
        $("input").blur(function(event) {
          if(event.target.value.length === 0){
            //label动态下降，恢复原位
            $(this).siblings("label").stop().animate({bottom: "10px"}, 300);
            //用div模拟的下边框缩回，其width动态恢复为默认宽度0
            $(this).next(".bottom-line").stop().animate({"width": "0"}, 300);
          }
        });

    },
    data() {
      const validateUsername = (rule, value, callback) => {
        const usernameReg = /^[A-Za-z0-9]+$/;
        if (!usernameReg.test(value)) {
          callback(new Error("Please enter the correct username"));
        } else {
          callback();
        }
      };
      const validateEmail = (rule, value, callback) => {
        const emailReg = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (!emailReg.test(value)) {
          callback(new Error("Please enter the correct email address"));
        } else {
          callback();
        }
      };

      return {
          vdaH:'',
        loginByuser:true,
        byuserType :"primary",
        byemailType : "info",
        form: {
          email:'',
          username: '',
          password: '',
        },
        rules: {
          email: [{required: true, message: "Please enter email address", trigger: "blur",}, { validator: validateEmail, trigger: "blur" },],
          username: [{required: true, message: "Please enter email address", trigger: "blur",}, {validator: validateUsername, trigger: "blur" },],
          password: [{required: true, message: "Please enter password", trigger: "blur",},],
        },
      };
    },
    methods: {
      ...mapMutations(['setJwt','setUserName','setFirstName','setAvatar']),
      byuser(){
          this.loginByuser = true;
          this.byuserType = "primary";
          this.byemailType = "info";
      },
      byemail(){
        this.loginByuser = false;
        this.byuserType = "info"
        this.byemailType = "primary";
      },




      signInEmail() {
        this.$refs["form"].validate((valid) => {
          if (valid) {
            let data = this.$qs.stringify(this.form);
            this.$axios.post('/user/emailLogin', data)
                    .then((response) => {
                      if (response.status >= 200 && response.status < 300) {
                        if(response.data.code === 200){
                          this.$store.commit('setJwt', response.headers.jwt);
                          this.$store.commit('setUserName', this.form.username);
                          this.$store.commit('setAvatar', response.data.result.avatar);
                          this.$store.commit('setFirstName', response.data.result.firstname);
                          this.$router.push({name: 'home'});
                          console.log(response.data);
                        }else if(response.data.code === 400){
                          this.$message.error(response.data.msg);
                        }else{
                          console.log(response.data.msg);
                        }
                      } else {
                        console.log(response.data.msg);
                      }
                    })
                    .catch((res) => {
                      console.log('error ', res);
                      this.$message.error('Login failed!');
                    })
          } else {
            return false;
          }
        });
      },
      signInUser() {
        this.$refs["form"].validate((valid) => {
          if (valid) {
            let data = this.$qs.stringify(this.form);
            this.$axios.post('/user/login', data)
                    .then((response) => {
                      if (response.status >= 200 && response.status < 300) {
                        if(response.data.code === 200){
                          this.$store.commit('setJwt', response.headers.jwt);
                          this.$store.commit('setUserName', this.form.username);
                          this.$store.commit('setAvatar', response.data.result.avatar);
                          this.$store.commit('setFirstName', response.data.result.firstname);
                          this.$router.push({name: 'home'});
                          console.log(response.data);
                        }else if(response.data.code === 400){
                          this.$message.error(response.data.msg);
                          }else{
                            console.log(response.data.msg);
                          }
                      } else {
                        console.log(response.data.msg);
                      }
                    })
                    .catch((res) => {
                      console.log('error ', res);
                      this.$message.error('Login failed!');
                    })
          } else {
            return false;
          }
        });
      },
      forgetpwd(){
        this.$router.replace("/reset");
      },
      goto(name) {
        console.log(name);
        this.$router.push({ name: name });
      },
      back() {
        this.$router.go(-1);
      },
      mouseIn(){
        $(this).next(".bottom-line").stop().animate({"width": "400px"}, 300);
      }
    },
  };
</script>

<style scoped lang="scss">
  .title {
    margin-top: 20%;
    width:100%;
    text-align: center;
    /*border:1px solid #133264;*/
  }
  .form {
    padding: 30px;
    /*border: 1px solid #ccc;*/
    border-radius: 15px;
  }
  .forget-password {
    margin-top: 15px;
    text-align: left;
  }


  .content{
    width:100%;
    /*height:Height;*/
    /*  height: 100vh;*/
    /*margin:0 auto;*/
    background-color: #fff;
    /*border:1px solid #133264;*/
    margin-top:50px;
  }
  .details {
    padding:50px 50px 0;
    /*border:1px solid #133264;*/
  }
  .details .item{
    width:400px;
    margin:20px auto;
    position: relative;
    /*border:1px solid #133264;*/
  }
  .item  label{
    font-size: 16px;
    position: absolute;
    left:2px;
    bottom:10px;
    color:#777;
    cursor: text;
  }
  .img-size img{
    width: 62.5%;
    height: 100%;
    position:absolute;
  }
  .item input{
    padding-top: 35px;
    width:400px;
    height: 80px;
    font-size: 20px;
    border:none;
    outline: none;
    border-bottom: 1px solid #c3c3c3;
  }
  .item .bottom-line{
    position: absolute;
    width: 0;
    height: 4px;
    left:0;
    bottom: -1px;
    background-color: #3b4c73;
  }

  @keyframes show {
    0% {
    margin-left: 50px;
    opacity: 0;
  }
  100% {
    margin-left: 0;
    opacity: 1;
  }
}
@keyframes hide {
  0% {
    margin-left: 0;
    opacity: 1;
  }
  100% {
    margin-left: 10px;
    opacity: 0;
  }
}

.show-enter-active {
  animation: show 1s;
}
.show-leave-active {
  animation: hide 0.3s;
}
.show-enter{
  opacity: 0;
}
.show-leave-to {
  opacity: 1;
}

.btn-style{
  cursor: pointer;
  margin:20px 0;
  width:200px;
  float:right;
  position: relative;
}
.btn-style span{
  /*position: relative;*/
}
.btn-style .bottom-line{
  position: absolute;
  width: 0;
  height: 2px;
  left:0;
  bottom: -1px;
  background-color: #3b4c73;
}
</style>
