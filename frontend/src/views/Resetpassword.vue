<template>
  <div class="resetpassword">
    <Header>
      <el-button round type="primary" @click="back">Back</el-button>
    </Header>
    <el-row type="flex" justify="center">
      <el-col :span="9">
        <h1 class="title">Reset Password</h1>
        <el-form
          class="form"
          ref="form"
          :model="form"
          :rules="rules"
          label-width="150px"
          label-position="left"
        >
          <el-form-item label="Password" prop="password">
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
<!--          <el-form-item label="Validate:" prop="validate">-->
<!--            <el-input v-model="form.validate" placeholder="Press the Validate button to get the code"></el-input>-->
<!--          </el-form-item>-->
        </el-form>
        <div class="btns">
<!--          <div class="validate" @click="validate"  id="validate">-->
<!--            <span v-show="show">validate</span>-->
<!--            <span v-show="!show">{{ count }} s</span>-->
<!--          </div>-->
          <el-button round type="primary" @click="resetPassword">Submit</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
export default {
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
      form: {
        password: '',
        passwordAgain: '',
        // validate:''
      },
      rules: {
        password: [{required: true, message: "Please enter new password", trigger: "blur",},],
        passwordAgain: [{required: true, message: "Please enter the password again", trigger: "blur",}, { validator: validatePasswordAgain, trigger: "blur" },],
        // validate: [{required: true, message: "Please enter validate code", trigger: "blur",},],
      },
    };
  },
  methods: {
    resetPassword() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let data = this.$qs.stringify(this.form);
          this.$axios.post('/user/reset', data)
                  .then((response) => {
                    if (response.status >= 200 && response.status < 300) {
                      console.log(response.data);
                      this.$message.success("Reset password successful");
                      this.$router.replace("/alpha");
                    } else {
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
  display: flex;
  justify-content: flex-end;
}
</style>
