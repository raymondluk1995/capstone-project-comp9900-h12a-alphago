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
              <el-upload
                class="avatar-uploader"
                action="https://jsonplaceholder.typicode.com/posts/"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
              >
                <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <div class="btns">
                <el-button round type="primary" @click="register">Submit</el-button>
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
      const phoneReg = /^(?:(?:\+|00)61)?4\d{8}$/;
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
      },
      rules: {
        username: [{required: true, message: "Please enter username", trigger: "blur",},],
        firstname: [{required: true, message: "Please enter firstname", trigger: "blur",},],
        lastname: [{required: true, message: " Please enter lastname", trigger: "blur",},],
        phone: [{required: true, message: " Please enter phone", trigger: "blur",}, { validator: validatePhone, trigger: "blur" },],
        email: [{required: true, message: "Please enter email address", trigger: "blur",}, { validator: validateEmail, trigger: "blur" },],
        // payment: [{required: true, message: " Please enter payment", trigger: "blur",},],
        password: [{required: true, message: " Please enter password", trigger: "blur",},],
        passwordAgain: [{required: true, message: " Please enter password again", trigger: "blur",}, { validator: validatePasswordAgain, trigger: "blur" },],
      },
    };
  },
  methods: {
    ...mapMutations(["setFirstname"]),
    // Here, register wont sign in, push to login if registered
    register() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.$axios.post('/user/register', data)
                  .then((response) => {
                    if (response.status >= 200 && response.status < 300) {
                      console.log(response.data);
                      this.$message('Registration Successful!')
                      this.$router.replace("/login");
                    } else {
                      console.log(response.msg);
                    }
                  })
                  .catch((res) => {
                    console.log('error ', res);
                    this.$message.error('Registration Error');
                  });
        } else {
          return false;
        }
      });
    },
    handleAvatarSuccess(res, file) {
      this.form.imageUrl = URL.createObjectURL(file.raw);
    },
    goto(name) {
      console.log(name);
      this.$router.push({ name: name });
    },
    back() {
      this.$router.go(-1);
    },
  },
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
}
.btns {
  margin-top: 190px;
  text-align: right;
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
</style>
