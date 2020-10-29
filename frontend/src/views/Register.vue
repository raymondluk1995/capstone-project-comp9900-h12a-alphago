<template>
  <div class="register">
    <Header>
      <div class="back-btn">
        <span  id="login-btn" style="padding:2px 5px;font-size:20px;" @click="goto('login')">Sign In <i class="el-icon-check"></i></span>
        <div class="bottom-line"></div>
      </div>

      <div class="back-btn">
        <span  id="back-btn" style="padding:2px 5px;font-size:20px" @click="back">Back <i class="el-icon-refresh-right"></i></span>
        <div class="bottom-line"></div>
      </div>
    </Header>
    <el-row type="flex" justify="center">
      <el-col :span="8">
        <div class="img-size-reg" :style={height:vdaH}>
          <img src="../assets/reg-bg-2.png" alt="" >
        </div>
      </el-col>
      <el-col :span="16"style="background-color:#fff;box-shadow: inset 0px 15px 20px -15px rgba(70,92,132,0.45);">
        <h1 class="title">Sign Up</h1>
        <el-form
          class="form"
          ref="form"
          :model="form"
          :rules="rules"
        >
          <el-row  type="flex" justify="center" >
            <el-col :span="8" >
              <el-form-item label="" prop="username">
                <el-input v-model="form.username" placeholder="* Username" required="required"></el-input>
              </el-form-item>
              <el-row type="flex" justify="center">
                <el-col :span="12">
              <el-form-item label="" prop="firstname">
                <el-input v-model="form.firstname" placeholder="* Firstname"></el-input>
              </el-form-item>
                </el-col>
                <el-col :span="12">
              <el-form-item label="" prop="lastname">
                <el-input v-model="form.lastname" placeholder="* Lastname"></el-input>
              </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="" prop="phone">
                <el-input v-model="form.phone" placeholder="* Phone"></el-input>
              </el-form-item>
              <el-form-item label="" prop="email">
                <el-input style="width:60%;float:left;"  v-model="form.email"  ref="form.email" placeholder="* Email"></el-input>
                <el-button  style="width:35%;float:right;" v-show="show" type="" @click="validate"> <i class="el-icon-s-promotion"></i> Validate</el-button>
                <el-button  style="width:35%;float:right;" v-show="!show" type="">{{ count }} s</el-button>
              </el-form-item>
                <el-form-item label="" prop="validate">
              <el-input v-model="form.validate" placeholder="* Verify your Validate code here" ></el-input>
              </el-form-item>
              <el-form-item label="" prop="password">
                <el-input
                  type="password"
                  v-model="form.password"
                  show-password
                  placeholder="* Password"
                ></el-input>
              </el-form-item>
              <el-form-item label="" prop="passwordAgain">
                <el-input
                  type="password"
                  v-model="form.passwordAgain"
                  show-password
                  placeholder="* Password Again"
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="8" :offset="1" >
              <el-upload
                class="avatar-uploader"
                action="upload"
                :auto-upload="false"
                :show-file-list="false"
                :on-change="imgBroadcastChange"
                :before-upload="beforeAvatarUpload"
              >
                <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar"  alt=""/>
                <img v-else src="../assets/ava.png" class="el-icon-plus avatar-uploader-icon">
              </el-upload>

              <el-row style="text-align: center;">
                <p><i class="el-icon-upload2"></i> Upload Your Avatar</p>
              </el-row>

            </el-col>
          </el-row>
          <el-row>
            <div class="btns">
                <span class="button"
                      @click="register"
                      id="btns">
                    <i class="el-icon-right"></i> Submit</span>
              <div class="bottom-line"></div>
            </div>
          </el-row>
        </el-form>
      </el-col>

    </el-row>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import { mapMutations } from "vuex";
import $ from 'jquery'

export default {
  title: "User Registration",
  components: {
    Header,
  },
  created(){
    let h = document.documentElement.clientHeight  || document.body.clientHeight;
    this.vdaH = h - 147 + 'px';
  },
  mounted(){
    $("#back-btn").hover(function(event) {
      $(this).stop().animate({"margin-left": "10px"}, 300);
      $(this).next(".bottom-line").stop().animate({"width": "100px"}, 300);
    });
    $("#back-btn").mouseleave(function(event) {
      $(this).stop().animate({"margin-left": "0"}, 300);
      $(this).next(".bottom-line").stop().animate({"width": "0"}, 300);
    });

    $("#login-btn").hover(function(event) {
      $(this).stop().animate({"margin-left": "10px"}, 300);
      $(this).next(".bottom-line").stop().animate({"width": "100px"}, 300);
    });
    $("#login-btn").mouseleave(function(event) {
      $(this).stop().animate({"margin-left": "0"}, 300);
      $(this).next(".bottom-line").stop().animate({"width": "0"}, 300);
    });

    $("#btns").hover(function(event) {
      $(this).stop().animate({"margin-left": "10px"}, 300);
      $(this).next(".bottom-line").stop().animate({"width": "150px"}, 300);
    });
    $("#btns").mouseleave(function(event) {
      console.log('123');
      $(this).stop().animate({"margin-left": "0"}, 300);
      $(this).next(".bottom-line").stop().animate({"width": "0"}, 300);
    });


  },
  data() {
    const validateEmail = (rule, value, callback) => {
      const emailReg = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      if (!emailReg.test(value)) {
        callback(new Error("Please enter the correct email address"));
      } else {
        callback();
      }
    };
    const validatePhone = (rule, value, callback) => {
      const phoneReg = /^((?:61)|(?:0))?4\d{8}$/;
      if (!phoneReg.test(value)) {
        callback(new Error("Please enter the valid phone number"));
      } else {
        callback();
      }
    };
    const validatePasswordAgain = (rule, value, callback) => {
      let password = this.form.password;
      console.log(password);
      if (value !== password) {
        callback(new Error("Please enter the same password"));
      } else {
        callback();
      }
    };
    return {
      show: true,
      count: 60,
      timer: null,
      timerstart:false,
      form: {
        username:'',
        firstname: '',
        lastname: '',
        phone: '',
        email: '',
        // payment: '',
        password: '',
        passwordAgain: '',
        imageUrl: '',
        imageRaw:'',
      },
      rules: {
        username: [{required: true, message: "Please enter username", trigger: "blur",},],
        firstname: [{required: true, message: "Please enter firstname", trigger: "blur",},],
        lastname: [{required: true, message: " Please enter lastname", trigger: "blur",},],
        phone: [{required: true, message: " Please enter phone", trigger: "blur",}, { validator: validatePhone, trigger: "blur" },],
        email: [{required: true, message: "Please enter email address", trigger: "blur",}, { validator: validateEmail, trigger: "blur" },],
        validate: [{required: true, message: "Please enter validate code", trigger: "blur",},],
        password: [{required: true, message: " Please enter password", trigger: "blur",},],
        passwordAgain: [{required: true, message: " Please enter password again", trigger: "blur",}, { validator: validatePasswordAgain, trigger: "blur" },],
      },
    };
  },
  methods: {
    ...mapMutations(["setFirstname", 'setAvatar']),
    validate() {
        if ( this.timerstart === false) {
          this.$refs["form"].validateField('email',(valid) => {
            if (!valid) {
              let data = new FormData();
              data.append('email', this.form.email);
              this.$axios.post('/verify/register', data)
                      .then((response) => {
                        if (response.data.code === 400) {
                          this.$message.error('Email already exist!');
                          this.form.email = '';
                        }else if(response.data.code ===200) {
                          this.timerstart = true;
                          this.count = 60;
                          this.show = false;
                          $(".validate").addClass("huise")

                          // document.getElementById('validate').style.cursor = 'not-allowed'
                          this.timer = setInterval(() => {
                            if (this.count > 0 && this.count <= 60) {
                              this.count--
                            } else {
                              this.show = true
                              this.timer = null
                              clearInterval(this.timer)
                              this.timerstart = false
                            }
                          }, 1000)
                        }
                      })
                      .catch((res) => {
                        console.log('error', res);
                        this.$message.error('Validate Error');
                      });
               }
          })
        }
    },
    // Here, register wont sign in, push to login if registered
    register() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          // let data = this.$qs.stringify(this.form);
          let data = new FormData();
          data.append('username', this.form.username);
          data.append('firstname', this.form.firstname);
          data.append('lastname', this.form.lastname);
          data.append('phone', this.form.phone);
          data.append('email', this.form.email);
          data.append('password', this.form.password);
          data.append('validate', this.form.validate);
          data.append('avatar', this.form.imageRaw);
          console.log(data);
          this.$axios.post('/user/register', data)
                  .then((response) => {
                    if (response.data.code === 200) {
                      this.$store.commit('setAvatar', this.form.imageUrl);
                      this.$message('Registration Successful!');
                      this.$router.replace("/login");
                    } else if(response.data.code === 400){
                      this.$message.error(response.data.msg);
                    }
                    else {
                      console.log(response.data.msg);
                    }
                  })
                  .catch((res) => {
                    console.log('error', res);
                    this.$message.error('Registration Error');
                  });
        } else {
          return false;
        }
      });
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      let types = ['image/jpeg', 'image/jpg', 'image/png'];
      const isImage = types.includes(file.type);
      if (!isImage) {
        this.$message.error('上传图片只能是 JPG、JPEG、PNG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('Image size can not larger than 2MB!');
      }
      return isImage && isLt2M;
    },
    imgBroadcastChange(file) {
      this.form.imageRaw = file.raw;
      this.form.imageUrl = URL.createObjectURL(file.raw);
    },
    goto(name) {
      console.log(name);
      this.$router.push({name: name});
    },
    back() {
      this.$router.go(-1);
    },
  },
  watch:{
    timerstart: function(val){
      if(val === false){
        $(".validate").removeClass("huise")
        // document.getElementById('validate').style.cursor = 'pointer'
      }
    }
  }
};
</script>

<style lang="scss">
  .title {
    margin-top: 10%;
    width:100%;
    text-align: center;
    /*border:1px solid #133264;*/
  }
  .form {
    /*padding: 30px;*/
    /*border: 1px solid #ccc;*/
    border-radius: 0;
    /*margin-bottom: 50px;*/
  }
  .btns {
    /*border:1px solid #333;*/
    margin:5% auto 0 auto;
    /*margin-top: 75px;*/
    position: relative;
    text-align: center;
    width:150px;
    font-size:20px;
  }
  .btns .bottom-line{
    position: absolute;
    width: 0;
    height: 1px;
    left:5px;
    bottom: -1px;
    background-color: #3b4c73;
  }

.back-btn{
  /*border:1px solid #111111;*/
  width:150px;
}

  .validate:active{
    background-color: #0F996B;
  }
  .validate:hover{
    cursor: pointer;
    box-shadow: inset 0 0 100px 100px rgba(255, 255, 255, 0.2);
  }
  .avatar-uploader{
    text-align: center;
  }

  .avatar-uploader .el-upload {
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader-icon {
    border: 1px dashed #1b1b1b !important;
    border-radius: 50%;
    font-size: 28px;
    color: #8c939d;
    padding: 10px;
    width: 200px;
    height: 200px;
    /*line-height: 178px;*/
    /*text-align: center;*/
  }
  .avatar {
    border: 1px solid #1b1b1b !important;
    border-radius: 50%;
    width: 200px;
    height: 200px;
    display: block;
    padding: 5px;
  }
  .huise{
    background-color: #dcdcdc !important;
    color: black;
  }
  .validate{
    display:inline-block;
    width: 180px;
    height: 35px;
    background-color: rgb(7, 187, 127);
    margin: 0 auto 20px 0;
    line-height: 35px;
    font-family: PingFangSC-Regular;
    color: #ffffff;
    border-radius: 5px;
    -webkit-user-select:none;
    -moz-user-select:none;
    -ms-user-select:none;
    user-select:none;
  }

  .img-size-reg img{
    width:100%;
    height:100%;
  }

    .form input{
    border-radius:0;
    padding: 5px;
    /*width:400px;*/
    height: 40px;
    font-size: 15px;
    /*border:none;*/
    /*outline: none;*/
    /*border-bottom: 1px solid #c3c3c3;*/
  }

  .el-button{
    &:hover{
      background-color: rgba(217, 224, 239, 0.33) !important;
      color: #143469 !important;
      border:1px solid #9196a0  !important;
    }
  }
</style>
