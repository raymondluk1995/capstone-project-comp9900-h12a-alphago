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
          <el-form-item label="Username" prop="username">
            <el-input v-model="form.username"></el-input>
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
          <el-button round type="info" plane @click="forgetpwd">Forget Password?</el-button>
          <el-button round type="primary" @click="signIn" style="float: right;">Sign in</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import Header from "@/components/Header.vue";
  import {mapActions, mapMutations} from "vuex";

  export default {
    components: {
      Header,
    },
    data() {
      return {
        form: {
          username: '',
          password: '',
        },
        rules: {
          // TODO: check username format
          // username: [{required: true, message: "Please enter email address", trigger: "blur",}, { validator: validateEmail, trigger: "blur" },],
          username: [{required: true, message: "Please enter email address", trigger: "blur",},],
          password: [{required: true, message: "Please enter password", trigger: "blur",},],
        },
      };
    },
    methods: {
      // ...mapActions(['login']),
      ...mapMutations(['setJwt','setUserName','setFirstName']),
      signIn() {
        this.$refs["form"].validate((valid) => {
          if (valid) {
            let data = this.$qs.stringify(this.form);
            this.$axios.post('/user/login', data)
                    .then((response) => {
                      if (response.status >= 200 && response.status < 300) {
                        if(response.data.code === 200){
                          this.$store.commit('setJwt', response.jwt);
                          this.$store.commit('setUserName', this.form.username);
                          this.$store.commit('setAvatar', response.avatar);
                          this.$store.commit('setFirstName', response.data.result.firstname);
                          this.$router.push({name: 'home'});
                          console.log(response.data);
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
        // let username = this.$qs.stringify({username: this.form.username});
        // this.$axios.post('/user/reset', username)
        //         .then((response) => {
        //           if (response.status === 200) {
        //             this.$message('Confirmation mail has been sent to your email.');
        //             console.log('username is correct')
        //           } else if (response.status === 404) {
        //             this.$message('This username is not registered!');
        //             console.log(response.msg)
        //           }
        //         })
        //         .catch((res) => {
        //           console.log('error ', res);
        //           this.$message.error('Forget password backend Error');
        //         })
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
