<template>
    <div class="profile">
        <Header>
            <el-button round type="primary" @click="back">Back</el-button>
        </Header>
        <el-row type="flex" justify="center">
            <el-col :span="15">
                <h1 class="title">My Profile</h1>
                <el-form
                        class="form"
                        ref="form"
                        :model="form"
                        label-width="100px"
                        label-position="left"
                >
                    <el-row :gutter="50">
                        <el-col :span="14">
                            <el-form-item label="Username:" prop="username">
                                <el-input v-model="form.username" disabled></el-input>
                            </el-form-item>

                            <el-form-item v-if="!canEditEmail" label="Email:" prop="email">
                                <el-input style="width:70%;float:left;" v-model="form.oldemail" :disabled="!canEditEmail"></el-input>
                                <el-button round style="width:25%;float:right;" type="primary" @click="editE">Edit</el-button>
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
                                <el-input style="width:70%;float:left;" v-model="form.ofirstname" :disabled="!canEditFirstname"></el-input>
                                <el-button round style="width:25%;float:right;" type="primary" @click="editF">Edit</el-button>
                            </el-form-item>
                            <el-form-item v-else label="First Name:" prop="firstname">
                                <el-input style="width:70%;float:left;" placeholder="Change your first name" v-model="form.firstname"></el-input>
                                <el-button round style="width:25%;float:right;" type="ordinary" @click="cancelF">Cancel</el-button>
                                <el-button round style="width:25%;float:right;margin-top:10px;" type="success" @click="submitF">Submit</el-button>
                            </el-form-item>

                            <el-form-item v-if="!canEditLastname" label="Last Name:" prop="lastname">
                                <el-input style="width:70%;float:left;" v-model="form.olastname" :disabled="!canEditLastname"></el-input>
                                <el-button round style="width:25%;float:right;" type="primary" @click="editL">Edit</el-button>
                            </el-form-item>
                            <el-form-item v-else label="Last Name:" prop="lastname">
                                <el-input style="width:70%;float:left;" placeholder="Change your last name" v-model="form.lastname"></el-input>
                                <el-button round style="width:25%;float:right;" type="ordinary" @click="cancelL">Cancel</el-button>
                                <el-button round style="width:25%;float:right;margin-top:10px;" type="success" @click="submitL">Submit</el-button>
                            </el-form-item>

                        </el-col>
                        <el-col :span="6" :offset="1" >
                            <div class="user-avatar-container">
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
                                        console.log(response.msg);
                                    }
                                } else {
                                    console.log(response.msg);
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
                                    }else{
                                        console.log(response.msg);
                                    }
                                } else {
                                    console.log(response.msg);
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
                                    }else{
                                        console.log(response.msg);
                                    }
                                } else {
                                    console.log(response.msg);
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

                                    }else{
                                        console.log(response.msg);
                                    }
                                } else {
                                    console.log(response.msg);
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
                                }else if(response.data.code ===200){
                                    this.timerstart = true;
                                    this.count = 60;
                                    this.show = false;
                                    $(".validate").addClass("huise")

                                    // document.getElementById('validate').style.cursor = 'not-allowed'
                                    this.timer = setInterval(() => {
                                        if (this.count > 0 && this.count <= 60) {
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
                        ;
                    }
                    // if (!this.timer) {
                    //     this.count = 60;
                    //     this.show = false;
                    //     $(".validate").addClass("huise")
                    //
                    //     // document.getElementById('validate').style.cursor = 'not-allowed'
                    //     this.timer = setInterval(() => {
                    //         if (this.count > 0 && this.count <= 60) {
                    //             this.count--
                    //         } else {
                    //             this.show = true
                    //             clearInterval(this.timer)
                    //             this.timer = null
                    //         }
                    //     }, 1000)
                    // }
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
        margin: 30px;
        text-align: center;
    }
    .form {
        padding: 30px;
        border: 1px solid #ccc;
        border-radius: 15px;
    }
    .el-input.is-disabled .el-input__inner {
        background-color: #fff !important;
    }
    .btns {
        margin-top: 10px;
        text-align: center;
    }
    .user-avatar{
        margin-top: 20px;
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
        border-radius: 10%;
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
        border-radius: 10%;
    }
    .user-avatar-container {
        position: absolute;
        /*top: 180px;*/
        /*left: 15%;*/
        width: 20%;
        border: 0;
    }
    .huise{
        background-color: #dcdcdc !important;
        color: black;
    }
</style>
