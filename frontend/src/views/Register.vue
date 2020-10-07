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
          <el-row :gutter="50">
            <el-col :span="14">
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
                <el-input v-model="form.email"></el-input>
              </el-form-item>
                <el-form-item label="Validate Code:" prop="validate">
                  <el-input v-model="form.validate"></el-input>
              </el-form-item>
<!--              <el-form-item label="Payment:" prop="payment">-->
<!--                <el-input v-model="form.payment"></el-input>-->
<!--              </el-form-item>-->
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
              <!--                action="https://jsonplaceholder.typicode.com/posts/"-->
<!--              action="https://www.mocky.io/v2/5185415ba171ea3a00704eed/posts/"-->
              <el-upload
                class="avatar-uploader"
                action="upload"
                :auto-upload="false"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :on-change="imgBroadcastChange"
                :before-upload="beforeAvatarUpload"
              >
                <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <div class="btns">
                <div class="validate" @click="validate" id="validate">
                  <span v-show="show">validate</span>
                  <span v-show="!show">{{ count }} s</span>
                </div>
<!--                <el-button round type="ordinary" v-show="show"  @click="validate">validate</el-button>-->
<!--                <el-button round type="ordinary" v-show="!show">{{ count }} s</el-button>-->
                <el-button round type="primary" @click="register" id="submit-btn">Submit</el-button>
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
// import $ from 'jquery'

export default {
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
        // payment: [{required: true, message: " Please enter payment", trigger: "blur",},],
        password: [{required: true, message: " Please enter password", trigger: "blur",},],
        passwordAgain: [{required: true, message: " Please enter password again", trigger: "blur",}, { validator: validatePasswordAgain, trigger: "blur" },],
      },
    };
  },
  methods: {

    ...mapMutations(["setFirstname",'setAvatar']),
    validate(){
      // if (this.form.email === '' ) {
      //   alert('Email cannot be empty')
      // } else {
      //   if(this.timer == null){
      //     let data = new FormData();
      //     data.append('email', this.form.email);
      //     this.$axios.post('/verify/email', data);
      //   }
      //   if (!this.timer) {
      //     this.count = 60;
      //     this.show = false;
      //     $(".validate").addClass("huise")
      //
      //     // 将鼠标设置为不可点击状态
      //     document.getElementById('validate').style.cursor = 'not-allowed'
      //     this.timer = setInterval(() => {
      //       if (this.count > 0 && this.count <= 60) {
      //         this.count--
      //       } else {
      //         this.show = true
      //         clearInterval(this.timer)
      //         this.timer = null
      //       }
      //     }, 1000)
      //   }
      // }


      let data = new FormData();
      data.append('email',this.form.email);
      // todo time counter
      this.$axios.post('/verify/email', data);
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
                      console.log(response.data);
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
    handleAvatarSuccess(res, file) {
      // this.form.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    imgBroadcastChange(file){
      this.form.imageRaw = file.raw;
      this.form.imageUrl = URL.createObjectURL(file.raw);
    },
    // uploadAvatar (avatar) {
      // let formData = new FormData();
      // this.formData.append('avatar', avatar.file);
      // console.log(formData);
      // this.$axios.post('/user/upload_avatar', formData)
      //         .then((response) => {
      //           if (response.data.code === 200) {
      //             this.$store.commit('setAvatar', URL.createObjectURL(avatar.file));
      //             // this.imageUrl = URL.createObjectURL(avatar.file)
      //             // bus.$emit('updateUserAvatar', this.imageUrl)
      //           }
      //         })
      //         .catch(function (error) {
      //           console.log(error)
      //         })
    // },
    goto(name) {
      console.log(name);
      this.$router.push({ name: name });
    },
    back() {
      this.$router.go(-1);
    },
  // },
  // watch:{
  //   timer: function(val){
  //     console.log(val)
  //     if(val == null){
  //       //  监听timer变化，移除不可点击样式
  //       $(".validate").removeClass("huise")
  //       document.getElementById('validate').style.cursor = 'pointer'
  //     }
  //   }
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
// .validate{
//   float: left;
//   width: 120px;
//   height: 35px;
//   background-color: rgb(7, 187, 127);
//   margin: 0 auto 20px 0;
//   line-height: 35px;
//   font-family: PingFangSC-Regular;
//   color: #ffffff;
//   border-radius: 5px;
//   -webkit-user-select:none;
//   -moz-user-select:none;
//   -ms-user-select:none;
//   user-select:none;
// }

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
  width: 180px;
  height:35px;
  margin: 0 auto 20px 0;
  line-height: 35px;
  border-radius: 5px;
  -webkit-user-select:none;
  -moz-user-select:none;
  -ms-user-select:none;
  user-select:none;
  text-align: center;
  padding:0px;
  margin-top: 135px;
}
</style>
