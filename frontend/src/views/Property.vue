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
                        <el-dropdown-item command="property"  icon="el-icon-house"> My Properties</el-dropdown-item>
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
                        <h3>Latest Bid</h3>
                        <div class="bid"> ${{ propInfo.latestBid}}</div>
                        <h4  class="countdownTime">{{ time }}</h4>
                    </el-col>

<!--                    <el-col :span="5" :offset="7">-->
<!--                        <h3  >Time left</h3>-->
<!--                        <h4  >{{ time }}</h4>-->
<!--                    </el-col>-->
                </el-row>

                <el-dialog title="Select Your Card" :visible.sync="bidderFlag">
                    <template v-if="!addNewCard">
                        <el-radio-group v-model="defaultCard">
                            <el-radio :label="item" :key="item" v-for="item in cards" style="display:block; margin:20px 50px;">{{item}}</el-radio>
                        </el-radio-group>

                        <div slot="footer" class="dialog-footer">
                            <el-button @click="addCard">Add</el-button>
                            <el-button @click="reloadCard">Reload</el-button>
                            <el-button type="primary" @click="submit">Submit</el-button>
                        </div>
                    </template>

                    <template v-else>
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
                            <el-input v-model="form.cardNumber" @change="validateNum" maxlength="20"></el-input>
                        </el-form-item>
                        <el-form-item label="Expired Date:" prop="expiredDate">
                            <el-date-picker v-model="form.expiredDate" type="month">
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item label="CVC:" prop="cvc">
                            <el-input v-model="form.cvc"></el-input>
                        </el-form-item>
                    </el-form>

                    <div slot="footer" class="dialog-footer">
                        <el-button @click="backCard">Back</el-button>
                        <el-button type="primary" @click="submitCard">Add</el-button>
                    </div>
                    </template>
                </el-dialog>


                <section style="margin-top:50px">
                    <el-row>
                    <el-col >
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
                    </el-row>

                        <el-row>
                        <el-col >
                            <h3>Map</h3>
                            <div class="map" id="map">
                                <GmapMap
                                        :center="center"
                                        :zoom="15"
                                        map-type-id="roadmap"
                                        style="height: 300px"
                                >
                                    <GmapMarker
                                            :key="index"
                                            v-for="(m, index) in markers"
                                            :position="m.position"
                                            :clickable="true"
                                            :draggable="true"
                                    />
                                </GmapMap>
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

                        <div v-else style="margin-top: 70px">
                            <h3>Register as a Bidder</h3>
                            <el-button type="primary"
                                       style="font-size: 20px;"
                                       round
                                       :disabled="timeFlag"
                                       @click="Bidreg"
                                       icon="el-icon-right"
                                       plain
                            >Register to Bid</el-button>
                        </div>
                    </el-col>
                    </el-row>
                </section>

                <section style="margin-top:50px;">
                    <h3>Bid History</h3>
                </section>

                <section style="margin-top:50px; margin-bottom:200px">
                    <h3>Documents</h3>
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
                id:'',
                hasLogin: false,
                isBidder: false,
                bidderFlag: false,
                timeFlag: false,
                addNewCard:false,
                currentBid:'',
                newBid: '',
                newPlacedBid:'',
                tipError: false,
                time: '',
                cards:['111','222','333'],
                defaultCard:'',
                center:{lat:-33.9175679,lng:151.2255712},
                markers:[
                    {
                        position:{lat:-33.9175679,lng:151.2255712}
                    }
                ],
                propInfo: {
                    id: '',
                    // endDate: new Date(2000, 10, 10, 10, 10),
                    title: '',
                    endDate:'',
                    startDate:'',
                    info: '',
                    position: [],
                    detail: [],
                    latestBid: '',
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
            this.id = this.$route.query.id;
            this.$axios
                .get('/auction/information/' + this.id)
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
                this.countDown(this.propInfo.endDate,this.propInfo.startDate);
            }, 1000);
        },

        methods: {
            ...mapActions(["logout"]),
            handleCommand(command) {
                switch (command) {
                    case "profile":
                        this.$router.push("/profile");
                        break;
                    case "property":
                        this.$router.push("/propmag");
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

            countDown(time,startime) {
                let expiredTime = dayjs(time);
                let startTime = dayjs(startime);
                // let expiredTime = dayjs(new Date(2022, 10, 10, 10, 10));
                let nowTime = dayjs();

                let diff = expiredTime.diff(nowTime) / 1000;
                let diff2 = startTime.diff(nowTime) / 1000;

                let day = parseInt(diff / 3600 / 24);
                let hour = parseInt((diff / 3600) % 24);
                let minute = parseInt((diff / 60) % 60);
                let second = parseInt(diff % 60);

                if (diff2 >= 0) {
                    this.timeFlag = true;
                    this.time = 'This auction has not started yet!';
                }
                else{
                    this.time = ` ${day} : ${hour} : ${minute} : ${second} `;
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
            addCard() {
                this.addNewCard = true;
            },
            backCard() {
                this.addNewCard = false;
            },
            submitCard(){
                this.$refs["form"].validate((valid) => {
                    if (valid) {
                        let data = this.$qs.stringify(this.form);
                        this.$axios.post('/addcard', data)
                            .then((response) => {
                                if (response.status >= 200 && response.status < 300) {
                                    if(response.data.code === 200){
                                        this.$message.success("Add New Card successful!");
                                        this.addNewCard = false;
                                    }
                                } else if(response.data.code === 400){
                                    this.$message.error(response.msg);
                                    this.addNewCard = false;
                                }else{
                                    console.log(response.msg);
                                }
                            })
                            .catch((res) => {
                                console.log('error', res);
                                this.$message.error('Add Card Error');
                            });
                    } else {
                        return false;
                    }
                });

                this.$axios
                    .get('/card/information')
                    .then(response => {
                            this.cards = response.data.result
                    })
                    .catch(function (error) {
                        console.log(error)
                    })

            },
            reloadCard(){
                this.$axios
                    .get('/card/information')
                    .then(response => {
                        this.cards = response.data.result
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
            },

            submit() {
                this.$refs["form"].validate((valid) => {
                    if (valid) {
                        let data = new FormData();
                        data.append('defaultcard', this.defaultCard);
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
        padding: 10px;
        text-align: center;
        font-size: 25px;
        font-weight: bold;
        color: #fff;
        background-color: #133264;
        border-radius: 0;
    }
    .countdownTime{
        width :80%;
        padding: 10px;
        text-align: center;
        background-color: #c8dbf9;
        border-radius: 5px;
        font-size: 15px;

    }

    .el-tag {
        margin-right: 20px;
    }
    .map {
        /*margin-top:50px;*/
        height: 100%;
        width: 100%;
        /*border: #4d5861 1px solid;*/
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
