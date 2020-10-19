<template>
    <div class="property">
        <Header>
            <template v-if="this.hasLogin">
                <el-dropdown trigger="click" @command="handleCommand">
                    <div class="user">
                        <el-avatar :size="70" :src="avatar"></el-avatar>
                    </div>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="profile" icon="el-icon-user-solid"> My profile</el-dropdown-item>
                        <el-dropdown-item command="auction" icon="el-icon-s-home"> My Auctions</el-dropdown-item>
                        <el-dropdown-item command="notification"  icon="el-icon-bell"> Notifications</el-dropdown-item>
                        <el-dropdown-item command="logout" icon="el-icon-turn-off"> Log out</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
            <template v-else>
                <el-button round @click="goto('login')">Sign In</el-button>
                <el-button round type="primary" @click="goto('register')">Sign Up</el-button>
            </template>
        </Header>

        <el-row type="flex" justify="center">
            <el-col :span="16">
                <section style="margin-top: 15px">
                    <h1>{{ propInfo.title }}</h1>
                    <el-carousel :interval="5000" arrow="always" :height="cheight">
                        <el-carousel-item v-for="pic in propInfo.photos" :key="propInfo.pid">
                            <img :src="pic"  width="100%" height="100%" alt=""/>
                        </el-carousel-item>
                    </el-carousel>
                </section>


                <el-row>
                    <el-col :span="12">
                        <h3>Current Bid</h3>
                        <div class="bid"> ${{ propInfo.latestBid}}</div>
                    </el-col>

                    <el-col :span="5" :offset="7">
                        <h3  >Time left</h3>
                        <h4  >{{ time }}</h4>
                    </el-col>
                </el-row>

                <el-dialog title="Register as a bidder" :visible.sync="bidderFlag">
                    <el-form
                            class="form"
                            ref="form"
                            :model="form"
                            :rules="rules"
                            label-width="120px"
                            label-position="left"
                    >
                        <el-form-item label="Name:" prop="name">
                            <el-input v-model="form.name"></el-input>
                        </el-form-item>
                        <el-form-item label="Card Number:" prop="cardNumber">
                            <el-input v-model="form.cardNumber" @change="validateNum" maxlength="23"></el-input>
                        </el-form-item>
                        <el-form-item label="Expired Date:" prop="expiredDate">
                            <el-date-picker v-model="form.expiredDate" type="date">
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item label="CVC:" prop="cvc">
                            <el-input v-model="form.cvc"></el-input>
                        </el-form-item>
                    </el-form>

                    <div slot="footer" class="dialog-footer">
                        <el-button @click="bidderFlag = false">Cancel</el-button>
                        <el-button type="primary" @click="submit">Submit</el-button>
                    </div>
                </el-dialog>


                <section style="margin-top:50px">
                    <el-row :gutter="50">
                    <el-col :span="12">
                    <h3>Details</h3>
                    <el-row type="flex" style="margin-bottom: 10px;">
                            <i class="el-icon-toilet-paper"> Bathroom Number: <span> {{ propInfo.bathroomNum}} </span></i>
                    </el-row>

                    <el-row type="flex" style="margin-bottom: 10px;">
                            <i class="el-icon-house"> Bedroom Number: <span> {{ propInfo.bedroomNum}} </span></i>
                    </el-row>

                    <el-row type="flex" style="margin-bottom: 10px;">
                            <i class="el-icon-truck"> Garage Number: <span> {{ propInfo.garageNum}} </span></i>
                    </el-row>

                        <el-row type="flex" style="margin-bottom: 10px;">
                    <p>{{ propInfo.description }}</p>
                        </el-row>

                        <el-row type="flex" style="margin-bottom: 10px;">
                        <el-tag v-for="tag in propInfo.position" effect="plain">{{ tag }}</el-tag>
                        </el-row>
                            <el-row type="flex" style="margin-bottom: 10px;">
                            <el-tag v-for="tag in propInfo.detail">{{ tag }}</el-tag>
                        </el-row>
                    </el-col>




                    <el-col :span="12">
<!--                        <h3>Map</h3>-->
                        <div class="map">
                            <h3>...</h3>
                        </div>
                    </el-col>
                    </el-row>
                </section>

                <section style="margin-top:50px">
                    <el-row>
                    <el-col>
                        <div v-if="this.isBidder">
                            <h3>Place new bid</h3>
                            <div class="new-bid-wrap">
                                <el-input v-model="newBid" :disabled="timeFlag"></el-input>
                                <el-button class='wrap-button' type="primary" icon="el-icon-plus" circle @click="addNewBid"></el-button>
                            </div>

                            <p>{{ newBidTip }}</p>
                        </div>

                        <div v-else style="text-align:right;margin-top: 70px">
                            <el-button type="primary"
                                       style="font-size: 20px;"
                                       round
                                       :disabled="timeFlag"
                                       @click="Bidreg"
                                       icon="el-icon-right"
                            >Register to Bid</el-button>
                        </div>
                    </el-col>
                    </el-row>
                </section>

                <section style="margin-top:50px">
                </section>

            </el-col>
        </el-row>
    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
    import { mapActions } from "vuex";
    import dayjs from "dayjs";

    var customParseFormat = require("dayjs/plugin/customParseFormat");
    dayjs.extend(customParseFormat);

    export default {
        name: "Home",
        props: {
            cheight: {
                type: String,
                default: '500px'
            }
        },
        components: {
            Header,
        },
        data() {
            const validateCVC = (rule, value, callback) => {
                const cvcReg = /^\d{3}$/;
                if (!cvcReg.test(value)) {
                    callback(new Error("Please enter the valid cvc number"));
                } else {
                    callback();
                }
            };

            return {
                hasLogin: false,
                isBidder: true,
                bidderFlag: false,
                timeFlag: false,
                currentBid:'',
                newBid: '',
                newPlacedBid:'',
                tipError: false,
                time: '',

                propInfo: {
                    id: '',
                    endDate: '',
                    title: '',
                    info: '',
                    position: [],
                    detail: [],
                    latestBid: '',
                    newBid: '',
                    photos: [],
                    description: '',
                    bidHistory:[],
                },
                form: {
                    name: '',
                    cardNumber: '',
                    expiredDate: '',
                    cvc: '',
                },
                rules: {
                    name: [{required: true, message: " Please enter name", trigger: "blur",},],
                    cardNumber: [{required: true, message: " Please enter cardNumber", trigger: "blur",},],
                    expiredDate: [{required: true, message: " Please enter expired date", trigger: "blur",},],
                    cvc: [{required: true, message: " Please enter cvc", trigger: "blur",}, { validator: validateCVC, trigger: "blur" },],
                },
            };
        },

        created() {
            // this.username = localStorage.getItem("username");
            // // this.username = this.$store.state.username;
            // if (this.username !== null) {
            //     this.hasLogin = true;
            //     this.avatar = localStorage.getItem("avatar");
            //     this.firstname = localStorage.getItem("firstname");
            // }
            // else{
            //     this.$message.error("You should login first!");
            //     this.$router.push("/login");
            // }

            this.$axios
                .get('/property/information?pid=' + this.id)
                .then(response => {
                    this.form.propInfo = response.data.result.propInfo,
                    this.isBidder = response.data.result.isBidder,
                    this.currentBid = response.data.result.currentBid
                })
                .catch(function (error) {
                    console.log(error)
                })
        },

        computed: {
            newBidTip() {
                let tip = "Your current bid is 000,000";
                if (!this.newPlacedBid && !this.currentBid) {
                    tip = "Your current bid is 000,000";
                    this.tipError = false;
                } else if(!this.newPlacedBid && this.currentBid){
                    tip = `Your current bid is ${this.currentBid}`;
                    this.tipError = false;
                }else{
                    tip = `Your current bid is ${this.newPlacedBid}`;
                    this.tipError = false;
                }
                return tip;
            },
        },

        mounted() {
            let timer = setInterval(() => {
                if (this.timeFlag === true) {
                    clearInterval(timer);
                }
                this.countDown(this.propInfo.endDate);
            }, 1000);
        },

        methods: {
            ...mapActions(["logout"]),
            handleCommand(command) {
                switch (command) {
                    case "profile":
                        this.$router.push("/profile");
                        break;
                    case "auction":
                        this.$router.push("/auction");
                        break;
                    case "notification":
                        break;
                    case "logout":
                        this.$axios.post('/user/logout')
                            .then((response) => {
                                if (response.status >= 200 && response.status < 300){
                                    if (response.data.code === 200){
                                        this.logout();
                                        this.$router.replace("/");
                                    }else{
                                        console.log(response.msg)
                                    }
                                }else{
                                    console.log(response.msg)
                                }
                            })
                        break;
                    default:
                        break;
                }
            },
            validateNum () {
                let card = this.form.cardNumber.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/(\d{4})(?=\d)/g, '$1 ');
                this.$set(this.form, 'cardNumber', card)
            },

            countDown(time) {
                let expiredTime = dayjs(time);
                // let expiredTime = dayjs(new Date(2022, 10, 10, 10, 10));
                let nowTime = dayjs();
                let diff = expiredTime.diff(nowTime) / 1000;
                let day = parseInt(diff / 3600 / 24);
                let hour = parseInt((diff / 3600) % 24);
                let minute = parseInt((diff / 60) % 60);
                let second = parseInt(diff % 60);
                if (diff <= 0) {
                    this.timeFlag = true;
                    this.time = `00 :00 :00 :00 `;
                }else{
                    this.time = `${day} :${hour} :${minute} :${second} `;
                }
            },


            addNewBid() {
                const BidReg = /^-?\d+(,\d{3})*(\.\d{1,2})?$/;
                if (!BidReg.test(this.newBid)) {
                    this.$message({
                        type: "error",
                        message: "Please place correct bid!",
                    });
                    return false;
                }

                this.$confirm("Are you sure place the new bid?", "tip", {
                    confirmButtonText: "Submit",
                    cancelButtonText: "Cancel",
                    type: "warning",
                })
                    .then(() => {
                        let data = new FormData();
                        data.append('pid', this.id);
                        data.append('newBid', this.newBid);
                        this.$axios.post('/property/newbid', data)
                            .then((response) => {
                                if (response.status >= 200 && response.status < 300) {
                                    if(response.data.code === 200){
                                        this.currentBid = this.newBid;
                                        this.newPlacedBid = this.newBid;
                                        this.newBid='';
                                        this.$message({
                                            type: "success",
                                            message: "Place new bid successful!", })
                                    }else if(response.data.code === 400){
                                        this.$message.error(response.msg);
                                    }else{
                                        console.log(response.msg);
                                    }
                                } else {
                                    console.log(response.msg);
                                }
                            })
                            .catch((res) => {
                                console.log('error ', res);
                                this.$message.error('New Bid failed!');
                            })

                    })
                    .catch(() => {
                        this.$message({
                            type: "info",
                            message: "ERROR",
                        });
                    });
            },
            Bidreg() {
                this.bidderFlag = true;
            },

            submit() {
                this.$refs["form"].validate((valid) => {
                    if (valid) {
                        let data = this.$qs.stringify(this.form);
                        this.$axios.post('/bid/registration', data)
                            .then((response) => {
                                if (response.status >= 200 && response.status < 300) {
                                    if(response.data.code === 200){
                                        this.isBidder = true;
                                        this.bidderFlag = false;
                                        this.$message.success("Bid register successful!");
                                    }
                                } else if(response.data.code === 400){
                                    this.$message.error(response.msg);
                                    location.reload()
                                }else{
                                    console.log(response.msg);
                                }
                            })
                            .catch((res) => {
                                console.log('error', res);
                                this.$message.error('Bid Register Error');
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
        },
    };
</script>

<style scoped lang="scss">
    .user {
        display: flex;
        align-items: center;
        p {
            margin-left: 10px;
        }
    }

    p {
        line-height: 2.5;
    }
    h3 {
        margin-top: 30px;
        margin-bottom: 10px;
    }

    .el-date-editor.el-input,
    .el-date-editor.el-input__inner {
        width: 100%;
    }
    .bid {
        width :80%;
        padding: 5px;
        text-align: center;
        font-size: 20px;
        font-weight: bold;
        color: #fff;
        background-color: #133264;
        border-radius: 5px;
    }

    .el-tag {
        margin-right: 20px;
    }
    .map {
        margin-top:50px;
        height: 90%;
        width: 90%;
        float:right;
        border: #4d5861 1px solid;
    }
    .new-bid-wrap {
        display: flex;
        align-items: center;
        width:30%;
        .wrap-button{
            margin-left: 10px;
        }
    }

</style>
