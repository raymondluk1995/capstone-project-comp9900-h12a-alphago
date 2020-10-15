<template>
  <div class="register">
    <Header>
      <el-button round @click="goto('login')">Sign In</el-button>
      <el-button round type="primary" @click="back">Back</el-button>
    </Header>
    <el-row type="flex" justify="center">
      <el-col :span="14">
        <h1 class="title">Sign Up</h1>
        <el-form
          class="form"
          ref="form"
          :model="form"
          :rules="rules"
          label-width="150px"
          label-position="left"
        >
          <el-row :gutter="10">
            <el-col :span="15">
              <el-form-item label="Username:" prop="username">
                <el-input v-model="form.username"></el-input>
              </el-form-item>
              <el-form-item label="First Name:" prop="firstname">
                <el-input v-model="form.firstname"></el-input>
              </el-form-item>
              <el-form-item label="Last Name:" prop="lastname">
                <el-input v-model="form.lastname"></el-input>
              </el-form-item>
              <el-form-item label="Phone:" prop="phone">
                <el-input v-model="form.phone"></el-input>
              </el-form-item>
              <el-form-item label="Email:" prop="email">
                <el-input style="width:60%;float:left;"  v-model="form.email" ></el-input>
                <el-button round style="width:35%;float:right;" v-show="show" type="info" @click="validate">Validate</el-button>
                <el-button round style="width:35%;float:right;" v-show="!show" type="ordinary">{{ count }} s</el-button>
              </el-form-item>
                <el-form-item label="Validate Code:" prop="validate">
                  <el-input v-model="form.validate" placeholder="Press the Validate button to get the code"></el-input>
              </el-form-item>
              <el-form-item label="Password:" prop="password">
                <el-input
                  type="password"
                  v-model="form.password"
                  show-password
                ></el-input>
              </el-form-item>
              <el-form-item label="Password Again:" prop="passwordAgain">
                <el-input
                  type="password"
                  v-model="form.passwordAgain"
                  show-password
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" :offset="1">
              <el-upload
                class="avatar-uploader"
                action="upload"
                :auto-upload="false"
                :show-file-list="false"
                :on-change="imgBroadcastChange"
                :before-upload="beforeAvatarUpload"
              >
                <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <div class="btns">
<!--                <div class="validate" @click="validate"  id="validate">-->
<!--                  <span v-show="show">validate</span>-->
<!--                  <span v-show="!show">{{ count }} s</span>-->
<!--                </div>-->
                <el-button round type="primary" @click="register" id="submit-btn">Submit</el-button>
<!--                <el-button round type="primary" @click="register" >Submit</el-button>-->
              </div>
            </el-col>
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
  data() {
    const validateEmail = (rule, value, callback) => {
      const emailReg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
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
      if (this.form.email === '') {
        alert('Email cannot be empty')
      } else {
        if (this.timer == null) {
          let data = new FormData();
          data.append('email', this.form.email);
          this.$axios.post('/verify/register', data);
                  // .then((response) => {
                  //   if (response.data.code === 400) {
                  //     this.$message.error('Email already exist!');
                  //     this.form.email = '';
                  //   }else if(response.data.code ===200){
                  //     this.timerstart = true;
                  //   }
                  // })
                  // .catch((res) => {
                  //   console.log('error', res);
                  //   this.$message.error('Validate Error');
                  // });
        }
        if (!this.timer) {
          this.count = 60;
          this.show = false;
          $(".validate").addClass("huise")

          document.getElementById('validate').style.cursor = 'not-allowed'
          this.timer = setInterval(() => {
            if (this.count > 0 && this.count <= 60) {
              this.count--
            } else {
              this.show = true
              clearInterval(this.timer)
              this.timer = null
            }
          }, 1000)
        }
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
                    if (response.status >= 200 && response.status < 300) {
                      this.$store.commit('setAvatar', this.form.imageUrl);
                      this.$message('Registration Successful!');
                      this.$router.replace("/login");
                    } else {
                      console.log(response.msg);
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
    timer: function(val){
      console.log(val)
      if(val == null){
        $(".validate").removeClass("huise")
        document.getElementById('validate').style.cursor = 'pointer'
      }
    }
  }
};
</script>

<style scoped lang="scss">
  .title {
    margin: 30px;
    text-align: center;
  }
  .form {
    padding: 30px;
    border: 1px solid #ccc;
    border-radius: 15px;
    margin-bottom: 50px;
  }
  .btns {
    margin-top: 75px;
    text-align: center;
  }
  .validate:active{
    background-color: #0F996B;
  }
  .validate:hover{
    cursor: pointer;
    box-shadow: inset 0 0 100px 100px rgba(255, 255, 255, 0.2);
  }
  .avatar-uploader .el-upload {
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }
  .avatar-uploader-icon {
    border: 1px dashed #d9d9d9 !important;
    border-radius: 6px;
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
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
  #submit-btn{
    /*width: 180px;*/
    /*height:35px;*/
    /*margin: 0 auto 20px 0;*/
    /*line-height: 35px;*/
    /*border-radius: 5px;*/
    /*-webkit-user-select:none;*/
    /*-moz-user-select:none;*/
    /*-ms-user-select:none;*/
    /*user-select:none;*/
    /*text-align: center;*/
    /*padding:0px;*/
    margin-top: 100%;
  }
</style>
