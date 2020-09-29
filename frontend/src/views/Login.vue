<template>
  <div class="login">
    <Header>
      <el-button round type="primary" @click="back">Back</el-button>
    </Header>
    <el-row type="flex" justify="center" style="margin: 40px 0">
      <el-col :span="9">
        <h1 class="title">Sign In</h1>
        <el-form
          class="form"
          ref="form"
          :model="form"
          :rules="rules"
          label-width="100px"
          label-position="left"
        >
          <el-form-item label="Email" prop="email">
            <el-input v-model="form.email"></el-input>
          </el-form-item>
          <el-form-item label="Password" prop="password">
            <el-input
              type="password"
              v-model="form.password"
              show-password
            ></el-input>
          </el-form-item>
        </el-form>
        <div class="btns">
          <el-button round @click="goto('register')">Sign Up</el-button>
<!--          <el-button round type="info" plane @click="goto('reset')">Forget Password?</el-button>-->
          <el-button round type="info" plane @click="goto('reset')">Forget Password?</el-button>
          <el-button round type="primary" @click="signIn" style="float: right;">Sign in</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";

const validateEmail = (rule, value, callback) => {
  const emailReg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
  if (!emailReg.test(value)) {
    callback(new Error("Please enter the correct email address"));
  } else {
    callback();
  }
};

export default {
  components: {
    Header,
  },
  data() {
    return {
      form: {
        email: '',
        password: '',
      },
      rules: {
        email: [{required: true, message: "Please enter email address", trigger: "blur",}, { validator: validateEmail, trigger: "blur" },],
        password: [{required: true, message: "Please enter password", trigger: "blur",},],
      },
    };
  },
  methods: {
    signIn() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          // let email = this.form.email;
          // let avatar =
          //         'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=4286252317,3833864906&fm=26&gp=0.jpg';
          // // this.login({ email, avatar });
          // this.$message.success("Login successful");
          // this.$router.replace("/");
          let data = this.$qs.stringify(this.form)
          this.$axios.post('/user/login', data)
                  .then((response) => {
                    if (response.data.code === 200) {
                      console.log('login successfully')
                      this.$store.commit('$_setStorage', response.data.email)
                      this.$router.push({name: 'home'})
                    } else if (response.data.code === 400) {
                      this.$message.error('Wrong email or password')
                      console.log(response.data.msg)
                    }
                  })
                  .catch(function (error) {
                    console.log('ERROR')
                    console.log(error)
                  })
        } else {
          return false;
        }
      });
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
  margin-top: 30px;
  /*display: flex;*/
  justify-content: space-between;
}
.forget-password {
  margin-top: 15px;
  text-align: left;
}
</style>
