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
                <div class="back-btn">
                    <span  id="back-btn" style="padding:2px 5px;font-size:20px;" @click="goto('login')">Sign In <i class="el-icon-check"></i></span>
                    <div class="bottom-line"></div>
                </div>
                <div class="back-btn">
                    <span  id="back-btn2" style="padding:2px 5px;font-size:20px;" @click="goto('register')">Sign Up <i class="el-icon-user"></i></span>
                    <div class="bottom-line"></div>
                </div>
            </template>
        </Header>

        <el-row  type="flex" justify="center" style="background-color:#fff;box-shadow: inset 0px 15px 20px -15px rgba(70,92,132,0.45);">
            <el-col :span="16" style="margin: 0 50px 20px 30px; border:1px solid #ccc">
                <h1 style="margin: 50px 50px 0 50px">{{ propInfo.address }}</h1>
                <section style="margin: 15px 50px 0 50px">
<!--                    <h1>{{ propInfo.address }}</h1>-->
                    <el-row class="banner" :class="addStatusColor(propInfo.status)">
                        <h5 style="padding: 5px;">{{ time }}</h5>

                    </el-row>
                    <el-carousel :interval="5000" arrow="always" :height="cheight">
                        <el-carousel-item v-for="pic in propInfo.photos" :key="propInfo.pid">
                            <img :src="pic"  width="100%" height="100%" alt=""/>
                        </el-carousel-item>
                    </el-carousel>
                    <el-row>
                    </el-row>
                </section>

                <el-row type="flex" justify="center" style="margin: 15px 50px 0 50px">
                    <el-col :span="12" >
                        <div class="bid">
                            <span style="font-size:15px">Latest Bid</span>
                            ${{ propInfo.latestPrice | numFormat }}
                        </div>
                    </el-col>
                        <el-col :span="12">
                        <div class="bidder">
                            {{ propInfo.bidderNum }}  <span style="font-size:15px">Bidders</span>
                        </div>
                    </el-col>
                </el-row>


                <section style="margin: 15px 50px 0 50px">
                    <el-row>
                    <el-col >
<!--                    <h3>Details</h3>-->
                        <h3></h3>
                    <el-row type="flex" justify="left"  style="margin-bottom: 10px">
                        <el-col :span="4">
                        <div style="font-size: 20px">
                            <i class="el-icon-toilet-paper"><span> {{ propInfo.bathroomNum}} </span> Baths</i>
                        </div>
                    </el-col>
                        <el-col :span="4">
                        <div style="font-size: 20px">
                            <i class="el-icon-house"><span> {{ propInfo.bedroomNum}} </span> Beds</i>
                        </div>
                        </el-col>

                    <el-col :span="4">
                        <div style="font-size: 20px">
                            <i class="el-icon-truck"><span> {{ propInfo.garageNum}} </span> Garages</i>
                        </div>
                    </el-col>
                    </el-row>

                        <el-row type="flex" style="margin: 20px 0;">
                    <p>{{ propInfo.description }}</p>
                        </el-row>

                        <el-row type="flex" style="margin-bottom: 10px;">
                        <el-tag class='tag1' v-for="tag in propInfo.position.split(',')" effect="plain">{{ tag }}</el-tag>
                        </el-row>

                        <el-row type="flex" style="margin: 20px 0;">
                        <el-tag v-for="tag in propInfo.detail.split(',')">{{ tag }}</el-tag>
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
                                            v-for="m in markers"
                                            :position="m.position"
                                            :clickable="true"
                                            :draggable="true"
                                    />
                                </GmapMap>
                            </div>
                        </el-col>
                    </el-row>
                </section>


                <section style="margin: 15px 50px 0 50px;height:400px;">
                    <h3>Bid History</h3>

                    <el-table :data="propInfo.bidHistory"
                              :max-height="300"
                              border
                              stripe
                              tooltip-effect="light"
                              style="overflow-y: scroll; "
                    >

                        <template v-for="(item, index) in columns">
                            <el-table-column
                                    :key="index"
                                    :prop="item.prop"
                                    :label="item.label"
                                    :formatter="item.formatter"
                            >
                            </el-table-column>
                        </template>
                    </el-table>

                </section>

            </el-col>

            <el-col :span="5">
                <div class="info">
                    <el-row type="flex" justify="center" style="background-color: #4f6995;">
                        <h3 style="color:#f3f3f3">Seller</h3>
                    </el-row>
                <el-row type="flex" justify="center">
                    <el-avatar :size="70" :src="propInfo.avatar" style="margin-top:50px"></el-avatar>
                </el-row>

                    <el-row type="flex" justify="center" style="margin-top:20px">
                        <h4> {{ propInfo.firstname}} {{ propInfo.lastname}} </h4>
                    </el-row>

                    <el-row type="flex" justify="center" style="margin-top:20px">
                        <i class="el-icon-user-solid"><span> {{ propInfo.username}} </span></i>
                    </el-row>

                    <el-row type="flex" justify="center" style="margin-top:20px">
                        <i class="el-icon-phone"><span> {{ propInfo.phone}} </span></i>
                    </el-row>

                    <el-row type="flex" justify="center" style="margin-top:20px">
                        <i class="el-icon-message"><span> {{ propInfo.email}} </span></i>
                    </el-row>
                </div>

<!--                <el-button type="" :disabled="true" style="margin-top: 10px;color:#173b77;font-size: 20px;background-color:#a0b9df; width:100%"-->
<!--                >{{ propInfo.bidderNum }} Bidders</el-button>-->
                <template v-if="this.username !== null">
                <template v-if="username !== propInfo.username">
                    <div v-if="this.propInfo.rab !== null">
    <!--                    <h3>Place new bid</h3>-->
                        <div class="new-bid-wrap">
                            <el-input v-model="newBid" :disabled="timeFlag" placeholder="Place New Bid">
                                <i slot="suffix" class="input-slot">{{newBid | numFormat }} A$</i>
                            </el-input>
                            <el-button class='wrap-button' type="" icon="el-icon-plus" circle @click="addNewBid"></el-button>
                        </div>

                        <p style="color:rgba(78,102,146,0.35)">Your Current Bid is $ {{ propInfo.highestPrice | numFormat }}</p>
                    </div>

                    <div v-else style="margin-top: 2px">
                        <el-button type=""
                                   class="btn"
                                   @click="Bidreg"
                                   icon="el-icon-right"
                                   style="font-size:20px;"
                        >Register as RAB</el-button>
                    </div>
                    </template>
                <template v-else>
                    <div class="new-bid-wrap">
                    <el-button type="" :disabled="true" style="color:#f1f1f1;font-size: 20px;background-color:#3b4c73; width:100%">You are the Seller!</el-button>
                    </div>
                </template>
                </template>

                <template v-else>
                    <el-button @click="goto('login')">Login</el-button>
                </template>
            </el-col>
        </el-row>

        <el-dialog
                class="dialog"
                type="flex"
                title="Payment"
                :visible.sync="bidderFlag"
                style="position:absolute;left:25%; right:25%;"
                >
            <template>
                <el-row>
                    <el-row style="margin:20px 50px 0 50px;">
                    <span>Select your card:</span>
                    </el-row>

                    <el-row style="margin:20px 10px 10px 15%;width:70%;">
                    <el-radio-group v-model="selectCard" >
                        <el-radio
                                class="radio"
                                :label="item.paymentId"
                                :key="item.paymentId"
                                :disabled="addNewCard"
                                :value="item.paymentId"
                                v-for="item in cards"
                                border
                        >
                            {{showCard(item.cardNumber)}}
                        </el-radio>
                    </el-radio-group>
                    </el-row>

                </el-row>

                <el-row>
                    <div class="btns" style="margin-left: 20px">
                        <span  v-show="!addNewCard" id="add-btn" style="padding:2px 5px;" @click="addCard"><i class="el-icon-bottom"></i> Add New Card</span>
                    </div>
<!--                    <el-link v-show="!addNewCard" icon="el-icon-right" type="primary" @click="addCard">Add New Card</el-link>-->
                </el-row>

            </template>

            <template v-show="addNewCard">
                <el-row v-show="addNewCard" style="margin:20px 50px 0 50px;">
                    <span>Add your new card:</span>
                </el-row>
                <el-form
                        class="form"
                        ref="form"
                        :model="form"
                        :rules="rules"
                        v-show="addNewCard"
                >
<!--                    <el-form-item prop="name">-->
<!--                        <el-input v-model="form.name" placeholder="Name" clearable></el-input>-->
<!--                    </el-form-item>-->
<!--                    <el-form-item prop="cardNumber">-->
<!--                        <el-input v-model="form.cardNumber"  maxlength="19"  placeholder="Card Number"></el-input>-->
<!--                    </el-form-item>-->
<!--                    <el-row>-->
<!--                        <el-col :span=12>-->
<!--                            <el-form-item prop="expiredDate">-->
<!--                                <el-input v-model="form.expiredDate" placeholder="MM/YY"  maxlength="5"></el-input>-->
<!--                            </el-form-item>-->
<!--                        </el-col >-->
<!--                        <el-col :span=12>-->
<!--                            <el-form-item prop="cvc" >-->
<!--                                <el-input v-model="form.cvc" placeholder="CVC" maxlength="3"></el-input>-->
<!--                            </el-form-item>-->
<!--                        </el-col>-->
<!--                    </el-row>-->
                    <el-form-item prop="initPrice">
                        <el-input v-model="form.initialBid" placeholder="请输入内容"></el-input>
                    </el-form-item>
                </el-form>
                <el-row v-show="addNewCard">
                    <div class="btns"  style="margin-left: 20px">
                        <span id="cancel-btn" style="padding:2px 5px;" @click="notAdd"><i class="el-icon-right"></i> Cancel</span>
                    </div>

                </el-row>
<!--                <el-row style="margin:50px auto;width:70%;" prop="initPrice">-->
<!--                    <el-input v-model="this.initialBid" placeholder="请输入内容"></el-input>-->
<!--                </el-row>-->
            </template>

            <el-row >
                <div style="margin:50px 20px 30px 20px;float:right">
                    <div class="btns">
                        <span id="submit-btn" style="padding:2px 5px;" @click="submitCard"><i class="el-icon-right"></i> Submit</span>
                    </div>
                </div>
            </el-row>
        </el-dialog>
    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
    import { mapActions } from "vuex";
    import dayjs from "dayjs";
    import $ from 'jquery'
    import numFormat from "../utils/numFormat";

    var customParseFormat = require("dayjs/plugin/customParseFormat");
    dayjs.extend(customParseFormat);

    export default {
        name: "Home",
        props: {
            cheight: {
                type: String,
                default: '500px'
            },
        },
        components: {
            Header,
        },

        data() {
            return {
                websock: null,

                id:'',
                username:'',
                hasLogin: false,
                isBidder: false,
                bidderFlag: false,
                timeFlag: false,
                addNewCard:false,
                currentBid:'',
                rabId:'',
                newBid: '',
                newPlacedBid:'',
                tipError: false,
                time: '',
                cards:[
                //     {
                //     paymentId:'12',
                //     name:'Tom',
                //     cardNumber:'4321432143214321',
                //     cvc:'123',
                // },
                //     {
                //         paymentId:'11',
                //         name:'Bob',
                //         cardNumber:'1234123412341234',
                //         cvc:'012',
                //     },
                //     {
                //         paymentId:'15',
                //         name:'Tom',
                //         cardNumber:'4321432143214321',
                //         cvc:'123',
                //     }
                ],

                detail_tags:[],
                position_tags:[],
                selectCard:'',
                // center:{lat:-33.9175679,lng:151.2255712},
                lat :'',
                lng:'',
                center: {},
                initialBid:'',

                // markers:[{position:{lat:-33.9175679,lng:151.2255712}}],
                markers:[{position:{},}],
                columns: [
                    {prop: 'time', label: 'Time',width: '300',formatter: this.showTime_table},
                    {prop: 'user', label: 'User', width: '300'},
                    {prop: 'price', label: 'Current Bid',formatter: this.formatPrice}
                ],
                propInfo: {
                    id: '',
                    aid:'',
                    rab:'',
                    // endDate: new Date(2000, 10, 10, 10, 10),
                    username:'',
                    address: '',
                    enddate:'',
                    status:'',
                    startdate:'',
                    avatar:'',
                    bidderNum:'',
                    latestPrice:'',
                    info: '',
                    bedroomNum:'',
                    bathroomNum:'',
                    garageNum:'',
                    phone: '',
                    email:'',
                    position: '',
                    detail: '',
                    photos: [],
                    description: '',
                    bidHistory:[
                        // {
                        //     time: 1603981349 ,
                        //     user:'UMR',
                        //     price: '123123',
                        // },
                        // {
                        //     time: 1603981349,
                        //     user:'ooo',
                        //     price: '11100000',
                        // },
                        // {
                        //     time: 1603981349,
                        //     user:'TSF',
                        //     price: '123123',
                        // },



                    ],
                    firstname:'',
                    lastname:'',
                    highestPrice:'',
                    minimumPrice:'',
                },
                form: {
                    name: '',
                    cardNumber: '',
                    expiredDate: '',
                    cvc: '',
                },
                rules: {
                    name: [{required: true, message: " Please enter name", trigger: "blur",},],
                    cardNumber: [{required: true, message: " Please enter cardNumber", trigger: "blur",}, ,],
                    expiredDate: [{required: true, message: " Please enter expired date", trigger: "blur",},],
                    cvc: [{required: true, message: " Please enter cvc", trigger: "blur",},],
                },
            };
        },

        created() {
            this.username = localStorage.getItem("username");

            this.id = this.$route.query.id;
            this.$axios
                .get('/auction/information/' + this.id)
                .then(response => {
                    this.propInfo = response.data.result;
                    this.initWebSocket();
                    // this.isBidder = response.data.result.isBidder,
                    this.lat =  parseFloat(response.data.result.lat),
                    this.lng =  parseFloat(response.data.result.lng),
                    this.position_tags = response.data.result.position,
                    this.detail_tags = response.data.result.detail,
                    this.center = {
                        lat:this.lat,
                        lng:this.lng
                    },
                    this.markers = [{
                        position:{
                            lat:this.lat,
                            lng:this.lng
                        },
                    }]
                })
                .catch(function (error) {
                    console.log(error)
                });

            // if(this.propInfo.rabId !=='none'){
            //     this.$axios
            //         .get('/payment/get/')
            //         .then(response => {
            //             this.cards = response.data.result;
            //         })
            //         .catch(function (error) {
            //             console.log(error)
            //         });
            // }
        },

        watch: {
            ['form.cardNumber'](val) {
                this.$nextTick(() => {
                    this.form.cardNumber = val.replace(/\D/g,'').replace(/....(?!$)/g,'$& ');
                });
            },

            ['form.expiredDate'](val) {
                this.$nextTick(() => {
                    this.form.expiredDate = val.replace(/\D/g,'').replace(/..(?!$)/g,'$&\/');
                });
            }
        },

        computed: {
            // newBidTip() {
            //     let tip = "";
            //     if (!this.newPlacedBid && !this.currentBid) {
            //         tip = "0";
            //         this.tipError = false;
            //     } else if(!this.newPlacedBid && this.currentBid){
            //         tip = this.currentBid;
            //         this.tipError = false;
            //     }else{
            //         tip = this.newPlacedBid;
            //         this.tipError = false;
            //     }
            //     return tip;
            // },
        },

        mounted() {
            let timer = setInterval(() => {
                if (this.timeFlag === true) {
                    clearInterval(timer);
                }
                this.countDown(this.propInfo.enddate,this.propInfo.startdate);
            }, 1000);

            $("#back-btn").hover(function(event) {
                $(this).stop().animate({"margin-left": "10px"}, 300);
                $(this).next(".bottom-line").stop().animate({"width": "100px"}, 300);
            });

            $("#back-btn").mouseleave(function(event) {
                $(this).stop().animate({"margin-left": "0"}, 300);
                $(this).next(".bottom-line").stop().animate({"width": "0"}, 300);
            });

            $("#back-btn2").hover(function(event) {
                $(this).stop().animate({"margin-left": "10px"}, 300);
                $(this).next(".bottom-line").stop().animate({"width": "100px"}, 300);
            });

            $("#back-btn2").mouseleave(function(event) {
                $(this).stop().animate({"margin-left": "0"}, 300);
                $(this).next(".bottom-line").stop().animate({"width": "0"}, 300);
            });



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
                        this.$router.push("/auctionmag");
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


            notAdd(){
                this.form.name='';
                this.form.cardNumber= '';
                this.form.expiredDate= '';
                this.form.cvc='';
                this.addNewCard = false;
            },
            addStatusColor(status) {
                switch(status) {
                    case 'R':
                        return 'status-not-start1';
                        break;
                    case 'A':
                        return 'status-process1';
                        break;
                    default:
                        break;
                }
            },


            countDown(time,startime) {
                let expiredTime = dayjs(time);
                let startTime = dayjs(startime);
                // console.log(expiredTime.format("YYYY-MM-DD HH:mm:ss"));
                // console.log(startTime.format("YYYY-MM-DD HH:mm:ss"));
                // let startTime = dayjs(16400000);
                // let expiredTime = dayjs(new Date(2021, 2, 24, 17, 1));04102
                let nowTime = dayjs();

                let diff = expiredTime.diff(nowTime) / 1000;
                let diff2 = startTime.diff(nowTime) / 1000;
                // console.log(diff);
                // console.log(diff2);

                let day = parseInt(diff / 3600 / 24);
                let hour = parseInt((diff / 3600) % 24);
                let minute = parseInt((diff / 60) % 60);
                let second = parseInt(diff % 60);

                // console.log(this.showTime(startTime))

                if (diff2 > 0) {
                    this.timeFlag = true;
                    let st = dayjs(startTime).format("YYYY-MM-DD HH:mm:ss");
                    this.time = `Will start at ${ this.showTime2(st) }`;
                }
                else{
                    if(diff>0){
                        this.time = `Time Left: ${day} Days: ${hour} Hours: ${minute} Mins: ${second} Secs `;
                    }else{
                        this.timeFlag = false;
                        // console.log('over');
                        this.time = `This auction is Over!`;
                    }

                }
            },

            showTime(time){
                console.log(time)
                let st = dayjs(time).format("YYYY-MM-DD HH:mm:ss");
                return `${ st }`;
            },

            showTime_table(row,column){
                // console.log(row.time)
                let st = dayjs(row.time).format("YYYY-MM-DD HH:mm:ss");
                return `${ st }`;
            },

            showTime2(time){
                // let time = dayjs(day).format("YYYY-MM-DD HH:mm:ss");
                let MONTH =['Jan', 'Feb', 'Mar','Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                let day = dayjs(time).date();
                let mon = MONTH[dayjs(time).month()];
                let year = dayjs(time).year();
                let hour = dayjs(time).hour();
                let min = dayjs(time).minute();
                let formatTime = `${day} ${mon} ${year}, ${hour}:${min}`;
                return formatTime;
            },



            formatPrice(row,coloum) {
                // console.log(number)
                let temp =  row.price.toString().replace(/(\d)(?=(?:\d{3})+$)/g,'$1,')
                return `$ ${ temp }`;
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
                        data.append('rabId', this.rabId);
                        data.append('bidPrice', this.newBid);
                        this.$axios.post('/bid', data)
                            .then((response) => {
                                if (response.status >= 200 && response.status < 300) {
                                    if(response.data.code === 200){
                                        // this.currentBid = this.newBid;
                                        // this.newPlacedBid = this.newBid;
                                        this.newBid='';
                                        this.$message({
                                            type: "success",
                                            message: "Place new bid successful!", })
                                        location.reload();
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



            submitCard(){
                // if(this.addNewCard) {
                    this.$refs["form"].validate((valid) => {
                        if (valid) {
                            let data = new FormData();

                            // data.append('name', this.form.name);

                            // let card = this.form.cardNumber.replace(/\s+/g, "");
                            // data.append('cardNumber', card);
                            //
                            // data.append('expiryDate', this.form.expiredDate);
                            // data.append('cvv', this.form.cvc);
                            data.append('aid', this.id);

                            data.append('registerTime', dayjs().valueOf().toString())
                            data.append('initPrice',this.initialBid)
                            this.$axios.post('/rab/register', data)
                                .then((response) => {
                                    if (response.status >= 200 && response.status < 300) {
                                        if (response.data.code === 200) {
                                            this.rabId = response.data.result;
                                            this.$message.success("Register successful!");
                                            location.reload();
                                        }
                                    } else if (response.data.code === 400) {
                                        this.$message.error(response.msg);
                                    } else {
                                        console.log(response.msg);
                                    }
                                })
                                .catch((res) => {
                                    console.log('error', res);
                                    this.$message.error('Error');
                                });
                        } else {
                            return false;
                        }
                    });
                    this.addNewCard = false;

                // }
                // else{
                //     let data = new FormData();
                //     data.append('paymentId', this.selectCard);
                //
                //     this.$axios.post('/payment/old', data)
                //         .then((response) => {
                //             if (response.status >= 200 && response.status < 300) {
                //                 if (response.data.code === 200) {
                //                     this.$message.success("Register successful!");
                //                     location.reload();
                //                 }
                //             } else if (response.data.code === 400) {
                //                 this.$message.error(response.msg);
                //             } else {
                //                 console.log(response.msg);
                //             }
                //         })
                //         .catch((res) => {
                //             console.log('error', res);
                //             this.$message.error('Register Error');
                //         });
                // }
                this.bidderFlag = false;
            },

            goto(name) {
                console.log(name);
                this.$router.push({ name: name });
            },

            // to update the highest bid
            initWebSocket(){
                console.log(this.propInfo.aid);
                const uri =  `ws://127.0.0.1:8070/auction/${this.propInfo.aid}`;
                console.log(uri);
                this.websock = new WebSocket(uri);
                this.websock.onmessage = this.websocketonmessage;
                this.websock.onopen = this.websocketonopen;
                this.websock.onerror = this.websocketonerror;
                this.websock.onclose = this.websocketclose;
            },
            websocketonopen(){ //连接建立之后执行send方法发送数据
                let actions = {"test":"12345"};
                this.websocketsend(JSON.stringify(actions));
            },
            websocketonerror(){//连接建立失败重连
                this.initWebSocket();
            },
            websocketonmessage(e){ //数据接收
                let res = JSON.parse(e.data);
                this.currentBid = res.bid;

                let Time = showTime(res.time);
                this.propInfo.bidHistory.push({time:Time, user:res.user, price:res.bid});

                this.notice(res.user);


            },
            websocketsend(Data){//数据发送
                this.websock.send(Data);
            },
            websocketclose(e){  //关闭
                console.log('close',e);
            },

            notice(user) {
                const h = this.$createElement;
                this.$notify({
                    title: 'Bid Update!',
                    message: h('i', { style: 'color: teal'},  `User ${user} becomes the winner!`)
                });
            },
            showCard(card){
                var reg = /^(\d{4})\d+(\d{4})$/;
                return card.replace(reg, "$1 **** **** $2");
            }

        },
        destroyed() {
            this.websock.close() //离开路由之后断开websocket连接
        },
    };
</script>

<style lang="scss">
    .form{
        margin-left:10%;
        /*margin-top:20px;*/
        width:80%;
    }
    .property{
        /*background-color: #aaaaaa;*/
    }
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
    .info{
        width:100%;
        height:500px;
        margin-top:50px;
        border:1px solid #ccc;
        /*box-shadow: 2px 1px 5px 4px #d5dbea;*/
    }
    .bid {
        padding: 15px;
        text-align: center;
        font-size: 25px;
        font-weight: bold;
        color: #fff;
        background-color: rgba(24, 64, 128, 0.82);
        /*border-radius: 5px;*/
        /*border-left: 15px solid #133264;*/
    }
    .bidder{
        padding: 15px;
        text-align: center;
        font-size: 25px;
        font-weight: bold;
        color: #184080;
        background-color: rgba(34, 91, 182, 0.25);
        /*border-radius: 5px;*/
        /*border-left: 15px solid #133264;*/
    }
    .countdownTime{
        width :80%;
        padding: 10px;
        text-align: center;
        /*background-color: #c8dbf9;*/
        border-radius: 5px;
        font-size: 15px;
    }
    .banner{
        width :100%;
        padding-top: 10px;
        align-items: center;
        text-align: center;
        background-color: #becdd6;
        /*border-radius: 5px;*/
        font-size: 10px;
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
        /*align-items: center;*/
        width:100%;
        margin-top:20px;
        .wrap-button{
            margin-left: 30px;
        }
    }
    .btn{
        font-size: 20px;
        background-color:#173b77;
        width:100%;
        color:#f3f3f3;
        /*&:hover {*/
        /*    cursor: pointer;*/
        /*    transform: scale(1.02);*/
        /*}*/
    }
    .status-process1 {
        background-image: url("../assets/banner-bg-green.png");
        /*background-color: #89c668;*/
    }
    /*.status-process {*/
    /*    background-color: #e6a23c;*/
    /*}*/

    .status-not-start1 {
        background-image: url("../assets/banner-bg-red.png");
        /*background-color: #f56c6c;*/
    }
    .back-btn{
        cursor: pointer;
        position:relative;
        width:150px;
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

    .el-tag{
        padding: 0 10px !important;
        height:30px !important;
        border-radius: 0 !important;
        border-left:2px solid #133264 !important;
        border-bottom:0;
        border-right:0;
        border-top:0;
        color: rgba(24, 64, 128, 0.51) !important;
        font-size: 18px !important;
    }

    .el-table__header{
        font-size: 20px !important;
    }
    .el-dialog__header{
        padding:20px 20px 10px;
        background-color: #2f4764;
    }
    .el-dialog__title{
        color:white !important;
    }
    .table-header{
        background-color:#4f6995;
        color:white
    }

    .btns{
        cursor: pointer;
        position:relative;
        width:150px;
        font-size:15px;
        /*border:1px solid #123123;*/
        &:hover{
            transform:translateX(10px);
            transition-duration: 0.5s;
            border-left:2px solid #184080;

        }
    }



    .el-radio{
        margin-left:0 !important;
    }

</style>
