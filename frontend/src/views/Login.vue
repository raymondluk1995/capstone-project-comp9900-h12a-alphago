<template>
  <div class="login">
    <Header>
      <el-button round type="primary" @click="back">Back</el-button>
    </Header>
    <el-row type="flex" justify="center" style="margin: 20px 0">
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
          <el-row type="flex" justify="center" style="margin-bottom: 20px">
            <el-button type="primary" icon="el-icon-user-solid" circle @click="byuser"></el-button>
            <el-button type="info" icon="el-icon-message" circle @click="byemail"></el-button>
          </el-row>

          <el-form-item v-if="loginByuser"  label="Username:" prop="username">
          <el-input v-model="form.username"></el-input>
        </el-form-item>

          <el-form-item v-else label="Email:" prop="email">
            <el-input v-model="form.email"></el-input>
          </el-form-item>

          <el-form-item label="Password:" prop="password">
            <el-input
                    type="password"
                    v-model="form.password"
                    show-password
            ></el-input>
          </el-form-item>
        </el-form>
        <div class="btns">
          <el-button round @click="goto('register')">Sign Up</el-button>
          <el-button round type="info" plane @click="forgetpwd">Forget Password?</el-button>
          <el-button v-if="loginByuser" round type="primary" @click="signInUser" style="float: right;">Sign in</el-button>
          <el-button v-else round type="primary" @click="signInEmail" style="float: right;">Sign in</el-button>
        </div>
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
        loginByuser:true,
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
      },
      byemail(){
        this.loginByuser = false;
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
                          this.$message.error('Password Incorrect!');
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
