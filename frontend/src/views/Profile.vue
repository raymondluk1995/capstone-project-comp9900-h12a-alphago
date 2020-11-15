<template>
    <div class="profile">
        <Header>
            <div class="back-btn">
                <span  id="back-btn" style="padding:2px 5px;font-size:20px;" @click="back">Back <i class="el-icon-refresh-right"></i></span>
                <div class="bottom-line"></div>
            </div>
        </Header>

        <el-row type="flex" justify="center">
            <el-col :span="10" >
                <div class="img-size" :style={height:vdaH}>
                    <img src="../assets/profile.png" alt="" >
                </div>
            </el-col>

            <el-col :span="14" style="background-color:#fff;box-shadow: inset 0 15px 20px -15px rgba(70,92,132,0.45);">
                <el-row tyle="flex" justify="space-around" style="margin-top:100px">
<!--                <el-col :span="15" >-->
<!--&lt;!&ndash;                    <span style="font-size: 30px">Hello!    </span>&ndash;&gt;-->
<!--                    <h3 class="title">{{form.username}}</h3>-->
<!--                </el-col>-->
                <el-col :span="24" >
                    <div class="user-avatar-container" >
                        <div class="user-avatar">
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
                        </div>
                        <template v-if="changeA">
                            <div class="user-avatar-button">
                                <el-row type="flex" justify="space-around" style="align-items: center;">
                                    <el-button icon="el-icon-close" circle plain  @click="cancelA" ></el-button>
                                    <el-button icon="el-icon-check" type="success"  circle @click="submitA"></el-button>
                                </el-row>
                            </div>
                        </template>
                    </div>
                    <h3 class="title" style="margin-top:20px">{{form.username}}</h3>
                </el-col>
                </el-row>

                    <el-row type="flex" justify="center" >
                        <el-form
                                class="formprofile"
                                ref="form"
                                :model="form"
                                label-width="100px"
                                label-position="left"
                        >
                        <el-col :span="24">
                            <el-row tyle="flex" justify="center" style="margin:10px 5%">
                            <el-form-item v-if="!canEditEmail" label="Email:" prop="email">
                                <span> {{form.oldemail}} </span>
<!--                                <el-input style="width:70%;float:left;" v-model="form.oldemail" :disabled="!canEditEmail"></el-input>-->
                                <el-button round style="width:25%;float:right;" type="primary" @click="editE"><i class="el-icon-edit"></i> Edit</el-button>
                            </el-form-item>
                            <el-form-item v-else label="Email:" prop="email">
                                <el-input style="width:70%;float:left;" placeholder="Input your new email" v-model="form.email"></el-input>
                                <el-button round style="width:25%;float:right;" type="ordinary" @click="cancelE">Cancel</el-button>
                                <el-input
                                        style="width:70%;float:left;margin-top:10px;"
                                        label="Validate:"
                                        placeholder="Validate Code"
                                        v-model="form.validate"
                                ></el-input>
                                <el-button round style="width:25%;float:right;margin-top:10px;" type="info" @click="validate" v-show="show">Validate</el-button>
                                <el-button round style="width:25%;float:right;margin-top:10px;" type="ordinary" v-show="!show">{{ count }} s</el-button>
                                <el-button round style="width:25%;float:right;margin-top:10px;" type="success" @click="submitE">Submit</el-button>
                            </el-form-item>

                            <el-form-item v-if="!canEditFirstname" label="First Name:" prop="firstname">
                                <span> {{form.ofirstname}} </span>
<!--                                <el-input style="width:70%;float:left;" v-model="form.ofirstname" :disabled="!canEditFirstname"></el-input>-->
                                <el-button round style="width:25%;float:right;" type="primary" @click="editF"><i class="el-icon-edit"></i> Edit</el-button>
                            </el-form-item>
                            <el-form-item v-else label="First Name:" prop="firstname">
                                <el-input style="width:70%;float:left;" placeholder="Change your first name" v-model="form.firstname"></el-input>
                                <el-button round style="width:25%;float:right;" type="ordinary" @click="cancelF">Cancel</el-button>
                                <el-button round style="width:25%;float:right;margin-top:10px;" type="success" @click="submitF">Submit</el-button>
                            </el-form-item>

                            <el-form-item v-if="!canEditLastname" label="Last Name:" prop="lastname">
                                <span> {{form.olastname}} </span>
<!--                                <el-input style="width:70%;float:left;" v-model="form.olastname" :disabled="!canEditLastname"></el-input>-->
                                <el-button round style="width:25%;float:right;" type="primary" @click="editL"><i class="el-icon-edit"></i> Edit</el-button>
                            </el-form-item>
                            <el-form-item v-else label="Last Name:" prop="lastname">
                                <el-input style="width:70%;float:left;" placeholder="Change your last name" v-model="form.lastname"></el-input>
                                <el-button round style="width:25%;float:right;" type="ordinary" @click="cancelL">Cancel</el-button>
                                <el-button round style="width:25%;float:right;margin-top:10px;" type="success" @click="submitL">Submit</el-button>
                            </el-form-item>
                            </el-row>
                        </el-col>

<!--                        <el-col :span="6" :offset="1" >-->
<!--                            <div class="user-avatar-container" >-->
<!--                            <div class="user-avatar">-->
<!--                            <el-upload-->
<!--                                    class="avatar-uploader"-->
<!--                                    action="upload"-->
<!--                                    :auto-upload="false"-->
<!--                                    :show-file-list="false"-->
<!--                                    :on-change="imgBroadcastChange"-->
<!--                                    :before-upload="beforeAvatarUpload"-->
<!--                            >-->
<!--                                <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar" />-->
<!--                                <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
<!--                            </el-upload>-->
<!--                            </div>-->
<!--                                <template v-if="changeA">-->
<!--                                <div class="user-avatar-button">-->
<!--                                    <el-row type="flex" justify="space-around" style="align-items: center;">-->
<!--                                        <el-button icon="el-icon-close" circle plain  @click="cancelA" ></el-button>-->
<!--                                        <el-button icon="el-icon-check" type="success"  circle @click="submitA"></el-button>-->
<!--                                    </el-row>-->
<!--                            </div>-->
<!--                                </template>-->
<!--                            </div>-->
<!--                        </el-col>-->
                        </el-form>
                    </el-row>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
    import { mapMutations } from "vuex";
    import $ from 'jquery'


    export default {
        title: 'My Profile',
        components: {
            Header,
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
            return {
                changeA:false,
                show: true,
                count: 60,
                timer: null,
                canEditEmail: false,
                canEditFirstname: false,
                canEditLastname: false,
                avatarOriginal:'',
                timerstart:false,
                form: {
                    username: "",
                    firstname: "",
                    lastname: "",
                    ofirstname:'',
                    olastname:'',
                    email: "",
                    oldemail:'',
                    imageRaw:'',
                    imageUrl:'',
                    validate:'',
                },
                rules: {
                    firstname: [{required: true, message: " Please enter firstname", trigger: "blur",},],
                    validate: [{required: true, message: " Please enter validate code", trigger: "blur",},],
                    lastname: [{required: true, message: " Please enter lastname", trigger: "blur",},],
                    email: [{required: true, message: "Please enter email address", trigger: "blur",}, { validator: validateEmail, trigger: "blur" },],
                },
            };
        },
        created(){
                this.username = localStorage.getItem("username");
                // this.username = this.$store.state.username;
                if (this.username !== null) {
                    this.hasLogin = true;
                    this.avatar = localStorage.getItem("avatar");
                    this.firstname = localStorage.getItem("firstname");
                }
                else{
                    this.$message.error("You should login first!");
                    this.$router.push("/login");
                }

            this.form.imageUrl = localStorage.getItem('avatar');

            this.$axios
                .get('/user/information')
                .then(response => {
                        this.form.username = response.data.result.username,
                        this.form.oldemail = response.data.result.email,
                        this.form.olastname = response.data.result.lastname,
                        this.form.ofirstname = response.data.result.firstname
                        this.form.imageUrl = response.data.result.avatar
                })
                .catch(function (error) {
                    console.log(error)
                })
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
        },
        methods: {
            ...mapMutations(["setFirstname", "setAvatar"]),
            editE(){
                this.canEditEmail = true;
            },
            editL(){
                this.canEditLastname = true;
            },
            editF(){
                this.canEditFirstname = true;
            },
            cancelA(){
                this.form.imageUrl = localStorage.getItem('avatar')
                this.changeA=false;
                // this.form.imageUrl = 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2931000230,881740929&fm=11&gp=0.jpg'
            },
            cancelE(){
                this.canEditEmail = false;
            },
            cancelL(){
                this.canEditLastname = false;
            },
            cancelF(){
                this.canEditFirstname = false;
            },
            submitE(){
                this.canEditEmail = false;
                this.$refs["form"].validate((valid) => {
                    if (valid) {
                        let data = new FormData();
                        data.append('email', this.form.email);
                        data.append('validate', this.form.validate);
                        this.$axios.post('/user/information/changeEmail', data)
                            .then((response) => {
                                if (response.status >= 200 && response.status < 300) {
                                    if(response.data.code === 200){
                                        this.$message('Email Reset Successful!');
                                        this.form.email = response.data.result;
                                        location.reload()
                                    }else if(response.data.code === 400){
                                        this.$message.error("Validate code incorrect");
                                        this.form.validate = '';
                                    }
                                    else{
                                        console.log(response.data.msg);
                                    }
                                } else {
                                    console.log(response.data.msg);
                                }
                            })
                            .catch((res) => {
                                console.log('error', res);
                                this.$message.error('Reset Email Error');
                            });
                    } else {
                        return false;
                    }
                });
            },
            submitL(){
                this.canEditLastname = false;
                this.$refs["form"].validate((valid) => {
                    if (valid) {
                        let data = new FormData();
                        data.append('lastname', this.form.lastname);
                        this.$axios.post('/user/information/changeLastname', data)
                            .then((response) => {
                                if (response.status >= 200 && response.status < 300) {
                                    if(response.data.code === 200){
                                        this.$message('Last Name Reset Successful!');
                                        this.form.lastname = response.data.result;
                                        location.reload()
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
                                console.log('error', res);
                                this.$message.error('Reset Last Name Error');
                            });
                    } else {
                        return false;
                    }
                });
            },
            submitF(){
                this.canEditFirstname = false;
                this.$refs["form"].validate((valid) => {
                    if (valid) {
                        let data = new FormData();
                        data.append('firstname', this.form.firstname);
                        this.$axios.post('/user/information/changeFirstname', data)
                            .then((response) => {
                                if (response.status >= 200 && response.status < 300) {
                                    if(response.data.code === 200){
                                        this.$store.commit('setFirstName', response.data.result);
                                        this.$message('First Name Reset Successful!');
                                        this.form.firstname = response.data.result;
                                        location.reload()
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
                                console.log('error', res);
                                this.$message.error('Reset First Name Error');
                            });
                    } else {
                        return false;
                    }
                });
            },
            submitA(){
                this.$refs["form"].validate((valid) => {
                    if (valid) {
                        let data = new FormData();
                        data.append('avatar', this.form.imageRaw);
                        this.$axios.post('/user/information/changeAvatar', data)
                            .then((response) => {
                                if (response.status >= 200 && response.status < 300) {
                                    if(response.data.code === 200){
                                        this.$store.commit('setAvatar', response.data.result);
                                        this.$message('Avatar Reset Successful!');
                                        this.form.avatar = response.data.result;
                                        location.reload()

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
                                console.log('error', res);
                                this.$message.error('Reset Avatar Error');
                            });
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
                this.changeA=true;
            },
            validate() {
                    if ( this.timerstart === false) {
                        let data = new FormData();
                        data.append('email', this.form.email);
                        this.$axios.post('/verify/register',data)
                            .then((response) => {
                                if (response.data.code === 400) {
                                    this.$message.error('Email already exist!');
                                    this.form.email = '';
                                }else if(response.data.code === 200){
                                    this.timerstart = true;
                                    this.count = 180;
                                    this.show = false;
                                    $(".validate").addClass("huise")

                                    // document.getElementById('validate').style.cursor = 'not-allowed'
                                    this.timer = setInterval(() => {
                                        if (this.count > 0 && this.count <= 180) {
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
                }
            },
        watch:{
            timer: function(val){
                console.log(val)
                if(val == null){
                    $(".validate").removeClass("huise")
                    // document.getElementById('validate').style.cursor = 'pointer'
                }
            }
        }
    };
</script>

<style scoped lang="scss">
    .title {
        /*margin-top: 10%;*/
        width:100%;
        /*text-align: center;*/
    }
    .formprofile {
        width:60%;
        padding: 30px;
        /*border: 1px solid #ccc;*/
        /*border-radius: 15px;*/
    }
    .el-input.is-disabled .el-input__inner {
        background-color: #fff !important;
    }
    .btns {
        margin-top: 10px;
        text-align: center;
    }
    .user-avatar{
        /*margin-top: 20px;*/
        text-align: center;
    }
    .avatar-uploader .el-upload {
        cursor: pointer;
        border-radius: 10%;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
        border-color: #409eff;
    }
    .user-avatar-button{
        border-radius: 10%;
        margin-top: 10px;
    }
    .avatar-uploader-icon {
        border: 1px dashed #d9d9d9 !important;
        /*border-radius: 50%;*/
        border-radius: 50%;
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }
    .avatar {
        border-radius: 50%;
        width: 178px;
        height: 178px;
        display: block;
    }
    .user-avatar-container {
        margin:0 auto ;
        /*position: absolute;*/
        /*top: 180px;*/display: flex;
        justify-content: center;
        text-align: center;
        /*left: 15%;*/
        width: 20%;
        border: 0;
    }
    .huise{
        background-color: #dcdcdc !important;
        color: black;
    }

    .back-btn{
        cursor: pointer;
        position:relative;
        /*border:1px solid #123123;*/
    }
    .back-btn .bottom-line{
        position: absolute;
        /*margin-left:40px;*/
        width: 0;
        height: 2px;
        left:0;
        bottom: -1px;
        background-color: #3b4c73;
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
    .img-size img{
        width: 41.667%;
        height: 100%;
        /*position:absolute;*/
    }
</style>
