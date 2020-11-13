<template>
  <div class="resetpassword">
    <Header>
      <div class="back-btn">
        <span  id="back-btn" style="padding:2px 5px;font-size:20px;" @click="back">Back <i class="el-icon-refresh-right"></i></span>
        <div class="bottom-line"></div>
      </div>
    </Header>
    <el-row type="flex" justify="center">
      <el-col :span="15" style="background-color:#fff;box-shadow: inset 0px 15px 20px -15px rgba(70,92,132,0.45);">
        <h1 class="title">Reset Password</h1>
        <el-form
          class="form"
          ref="form"
          :model="form"
          :rules="rules"
        >
          <el-row  type="flex" justify="center">
            <el-col :span="12" >
          <el-form-item label="" prop="username">
            <el-input v-model="form.username" placeholder="* Username"></el-input>
          </el-form-item>
          <el-form-item label="" prop="password">
            <el-input type="password" v-model="form.password" placeholder="* Password" show-password></el-input>
          </el-form-item>
          <el-form-item label="" prop="passwordAgain">
            <el-input type="password" v-model="form.passwordAgain" placeholder="* Password Again" show-password></el-input>
          </el-form-item>
          <el-form-item label="" prop="validate">
            <el-input  style="width:70%;float:left;" v-model="form.validate" placeholder="* Verify your Validate code here"></el-input>
            <el-button  style="width:30%;float:right;" v-show="show" type="" @click="validate"> <i class="el-icon-s-promotion"></i> Validate</el-button>
            <el-button  style="width:30%;float:right;" v-show="!show" type="ordinary">{{ count }} s</el-button>
          </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <div class="btns-reset">
            <span class="button3"
                  @click="resetPassword"
                  id="btns">
                    <i class="el-icon-right"></i> Submit</span>
            <div class="bottom-line"></div>
            </div>
          </el-row>
        </el-form>

      </el-col>

      <el-col :span="11" >
        <div class="img-size-reg" :style={height:vdaH}>
          <img src="../assets/reset-bg-2.png" alt="" >
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
  created(){
    let h = document.documentElement.clientHeight  || document.body.clientHeight;
    this.vdaH = h - 147 + 'px';
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
  mounted(){
    $("#back-btn").hover(function(event) {
      $(this).stop().animate({"margin-left": "10px"}, 300);
      $(this).next(".bottom-line").stop().animate({"width": "100px"}, 300);
    });
    $("#back-btn").mouseleave(function(event) {
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
                      if (response.data.code === 200) {
                        console.log(response.data);
                        this.$message.success("Reset password successful");
                        this.$router.replace("/login");
                      } else if (response.data.code === 400) {
                        this.$message.error("Validate code incorrect");
                        this.form.validate = '';
                      } else {
                        console.log(response.msg);
                      }
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
        if (this.timerstart === false) {
          let data = new FormData();
          data.append('username', this.form.username);
          this.$axios.post('/verify/reset', data)
                  .then((response) => {
                    if (response.data.code === 400) {
                      this.$message.error('Username does not exist!');
                      this.form.username = '';
                    }else if(response.data.code === 200){
                      this.timerstart = true;
                      this.count = 180;
                      this.show = false;
                      $(".validate").addClass("validate-disabled")

                      // document.getElementById('validate').style.cursor = 'not-allowed'
                      this.timer = setInterval(() => {
                        if (this.count > 0 && this.count <= 180) {
                          this.count--
                        } else {
                          this.show = true
                          clearInterval(this.timer)
                          this.timerstart = false
                          this.timer = null
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
    margin-top: 20%;
    width:100%;
    text-align: center;
    /*border:1px solid #133264;*/
  }
.form {
  padding: 30px;
  /*border: 1px solid #ccc;*/
  /*height: 60%;*/
}
  .btns-reset {
    /*border:1px solid #333;*/
    margin:30% auto 0 auto;
    margin-top: 75px;
    position: relative;
    text-align: center;
    width:150px;
    font-size:20px;
  }

  .btns-reset .bottom-line{
    position: absolute;
    /*margin-left:40px;*/
    width: 0;
    height: 1px;
    left:2px;
    bottom: -1px;
    background-color: #3b4c73;
  }


.validate-disabled{
  /*background-color: #dcdcdc !important;*/
  color: black;
}

.img-size-reg img{
  width:100%;
  height:100%;
}

</style>
