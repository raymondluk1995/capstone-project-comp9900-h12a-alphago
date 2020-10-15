<template>
  <div class="resetpassword">
    <Header>
      <el-button round type="primary" @click="back">Back</el-button>
    </Header>
    <el-row type="flex" justify="center">
      <el-col :span="12">
        <h1 class="title">Reset Password</h1>
        <el-form
          class="form"
          ref="form"
          :model="form"
          :rules="rules"
          label-width="150px"
          label-position="left"
        >
          <el-form-item label="Username:" prop="username">
            <el-input v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item label="Password:" prop="password">
            <el-input type="password" v-model="form.password" show-password></el-input>
          </el-form-item>
          <el-form-item label="Password Again:" prop="passwordAgain">
            <el-input type="password" v-model="form.passwordAgain" show-password></el-input>
          </el-form-item>
          <el-form-item label="Validate:" prop="validate">
            <el-input  style="width:70%;float:left;" v-model="form.validate" placeholder="Press the Validate button to get the code"></el-input>
              <el-button round style="width:25%;float:right;" type="info" v-show="show" @click="validate">Validate</el-button>
              <el-button round  style="width:25%;float:right;" type="ordinary" v-show="!show">{{ count }} s</el-button>
          </el-form-item>
        </el-form>
        <div class="btns">
          <el-button round type="primary" @click="resetPassword">Submit</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import $ from 'jquery'

export default {
  title: "Reset Password",
  components: {
    Header,
  },
  data() {
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
        password: '',
        passwordAgain: '',
        validate:''
      },
      rules: {
        username: [{required: true, message: "Please enter username", trigger: "blur",},],
        password: [{required: true, message: "Please enter new password", trigger: "blur",},],
        passwordAgain: [{required: true, message: "Please enter the password again", trigger: "blur",}, { validator: validatePasswordAgain, trigger: "blur" },],
        validate: [{required: true, message: "Please enter validate code", trigger: "blur",},],
      },
    };
  },
  methods: {
    resetPassword() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          // let data = this.$qs.stringify(this.form);
          let data = new FormData();
          data.append('username', this.form.username);
          data.append('password', this.form.password);
          data.append('validate', this.form.validate);
          this.$axios.post('/user/reset', data)
                  .then((response) => {
                    if (response.status >= 200 && response.status < 300) {
                      if(response.data.code === 200){
                        console.log(response.data);
                        this.$message.success("Reset password successful");
                        this.$router.replace("/login");
                      }
                    } else if(response.data.code === 400){
                        console.log(response.data);
                        this.$message.error("Validate code incorrect");
                        location.reload()
                    }else{
                      console.log(response.msg);
                    }
                  })
                  .catch((res) => {
                    console.log('error', res);
                    this.$message.error('Reset Error');
                  });
        } else {
          return false;
        }
      });
    },
    back() {
      this.$router.go(-1);
    },
    validate() {
      if (this.form.username === '') {
        alert('Username cannot be empty')
      } else {
        if (this.timer == null) {
          let data = new FormData();
          data.append('username', this.form.username);
          this.$axios.post('/verify/reset', data)
                  .then((response) => {
                    if (response.data.code === 400) {
                      this.$message.error('Username does not exist!');
                      this.form.username = '';
                    }else if(response.data.code ===200){
                      this.timerstart = true;
                      this.count = 60;
                      this.show = false;
                      $(".validate").addClass("validate-disabled")

                      // document.getElementById('validate').style.cursor = 'not-allowed'
                      this.timer = setInterval(() => {
                        if (this.count > 0 && this.count <= 60) {
                          this.count--
                        } else {
                          this.show = true
                          clearInterval(this.timer)
                          this.timerstart = false
                        }
                      }, 1000)
                    }
                  })
                  .catch((res) => {
                    console.log('error', res);
                    this.$message.error('Reset Error');
                  });
          ;
        }
        // if (!this.timer) {
        //   this.count = 60;
        //   this.show = false;
        //   $(".validate").addClass("validate-disabled")
        //
        //   // document.getElementById('validate').style.cursor = 'not-allowed'
        //   this.timer = setInterval(() => {
        //     if (this.count > 0 && this.count <= 60) {
        //       this.count--
        //     } else {
        //       this.show = true
        //       clearInterval(this.timer)
        //       this.timer = null
        //     }
        //   }, 1000)
        // }
      }
    },
  },
  watch:{
    timerstart: function(val){
      if(val === false){
        $(".validate").removeClass("validate-disabled")
        // document.getElementById('validate').style.cursor = 'pointer'
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
}
.btns {
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
}
.validate-disabled{
  /*background-color: #dcdcdc !important;*/
  color: black;
}
</style>
