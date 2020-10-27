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
        <el-row  type="flex" justify="center" >
            <el-col :span="16" style="margin: 50px 50px 20px 30px; box-shadow: 2px 1px 5px 4px #d5dbea;">
                <h1 style="margin: 15px 50px 0 50px">{{ propInfo.address }}</h1>
                <section style="margin: 15px 50px 0 50px">
<!--                    <h1>{{ propInfo.address }}</h1>-->
                    <el-row class="banner" :class="addStatusColor(propInfo.status)">
                        <h4>{{ time }}</h4>
                    </el-row>
                    <el-carousel :interval="5000" arrow="always" :height="cheight">
                        <el-carousel-item v-for="pic in propInfo.photos" :key="propInfo.pid">
                            <img :src="pic"  width="100%" height="100%" alt=""/>
                        </el-carousel-item>
                    </el-carousel>
                </section>

                <el-row style="margin: 15px 50px 0 50px">
                    <el-col :span="12">
                        <h3>Latest Bid</h3>
                        <div class="bid"> ${{ propInfo.latestPrice }}</div>
<!--                        <h4  class="countdownTime">{{ time }}</h4>-->
                    </el-col>
                </el-row>

                <el-dialog type="flex" title="Payment" :visible.sync="bidderFlag" style="position:absolute;left:25%; right:25%;">
                    <template>
                        <el-row>
                        <el-radio-group v-model="selectCard">
                            <el-radio :label="item" :key="item" :disabled="addNewCard" v-for="item in cards" style="display:block; margin:20px 50px;">{{item}}</el-radio>
                        </el-radio-group>
                        </el-row>

                        <el-row style="margin-left:10%">
                        <el-link v-show="!addNewCard" icon="el-icon-right" type="primary" @click="addCard">Add New Card</el-link>
                        </el-row>
                        <div slot="footer" class="dialog-footer">
                            <el-button type="primary" @click="submitCard">Submit</el-button>
                        </div>
                    </template>

                    <template v-show="addNewCard">
                    <el-form
                            class="form"
                            ref="form"
                            :model="form"
                            :rules="rules"
                            v-show="addNewCard"
                    >
                        <el-form-item prop="name">
                            <el-input v-model="form.name" placeholder="Name" clearable></el-input>
                        </el-form-item>
                        <el-form-item prop="cardNumber">
                            <el-input v-model="form.cardNumber"  maxlength="19"  placeholder="Card Number"></el-input>
                        </el-form-item>
                        <el-row>
                        <el-col :span=12>
                        <el-form-item prop="expiredDate">
                            <el-input v-model="form.expiredDate" placeholder="MM/YY"  maxlength="5"></el-input>
                        </el-form-item>
                        </el-col >
                            <el-col :span=12>
                        <el-form-item prop="cvc" >
                            <el-input v-model="form.cvc" placeholder="CVC" maxlength="3"></el-input>
                        </el-form-item>
                        </el-col>
                        </el-row>
                        <el-row>
                        <el-link  icon="el-icon-top" type="primary" @click="notAdd">Cancel</el-link>
                        </el-row>
                    </el-form>

<!--                    <div slot="footer" class="dialog-footer">-->
<!--                        <el-button @click="backCard">Back</el-button>-->
<!--                        <el-button type="primary" @click="submitCard">Add</el-button>-->
<!--                    </div>-->
                    </template>
                </el-dialog>

                <section style="margin: 15px 50px 0 50px">
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
                        <el-tag v-for="tag in propInfo.position.split(',')" effect="plain" :key="propInfo.position">{{ tag }}</el-tag>
                        </el-row>

                        <el-row type="flex" style="margin-bottom: 10px;">
                        <el-tag v-for="tag in propInfo.detail.split(',')" :key="propInfo.detail">{{ tag }}</el-tag>
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


                <section style="margin: 15px 50px 0 50px;height:500px;border:1px solid #123123">
                    <h3>Bid History</h3>

                    <el-table
                            :data="propInfo.bidHistory"
                            stripe
                            style="width: 100%">
                        <el-table-column
                                prop="time"
                                label="Time"
                                width="300px">
                        </el-table-column>
                        <el-table-column
                                prop="user"
                                label="User"
                                width="300px">
                        </el-table-column>
                        <el-table-column
                                prop="price"
                                label="Bid Price">
                        </el-table-column>
                    </el-table>
                </section>

            </el-col>
            <el-col :span="5">
                <div class="info">
                    <el-row type="flex" justify="center" style="background-color: #133264;">
                        <h3 style="color:#f3f3f3">Owner</h3>
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

                <el-button type="" :disabled="true" style="margin-top: 10px;color:#173b77;font-size: 20px;background-color:#a0b9df; width:100%"
                >{{ propInfo.bidderNum }} Bidders</el-button>

                <template v-if="username !== propInfo.username">
                <div v-if="this.rab ==='none'">
<!--                    <h3>Place new bid</h3>-->
                    <div class="new-bid-wrap">
                        <el-input v-model="newBid" :disabled="timeFlag" placeholder="Place New Bid">
                            <i slot="suffix" class="input-slot">{{newBid |numFormat}} A$</i>
                        </el-input>
                        <el-button class='wrap-button' type="primary" icon="el-icon-plus" circle @click="addNewBid"></el-button>
                    </div>

                    <p>Your Current Bid is {{ newBidTip | numFormat}}</p>
                </div>

                <div v-else style="margin-top: 2px">
                    <el-button type=""
                               class="btn"
                               @click="Bidreg"
                               icon="el-icon-right"
                    >Register to Bid</el-button>
                </div>
                </template>
                <template v-else>
                    <div class="new-bid-wrap">
                    <el-button type="" :disabled="true" style="color:#f1f1f1;font-size: 20px;background-color:#3b4c73; width:100%">You are the Owner!</el-button>
                    </div>
                </template>
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
                newBid: '',
                newPlacedBid:'',
                tipError: false,
                time: '',
                cards:[],
                detail_tags:[],
                position_tags:[],
                selectCard:'',
                // center:{lat:-33.9175679,lng:151.2255712},
                lat :'',
                lng:'',
                center: {},
                // markers:[{position:{lat:-33.9175679,lng:151.2255712}}],
                markers:[{position:{},}],
                propInfo: {
                    id: '',
                    aid:'',
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
                    phone: '',
                    email:'',
                    position: '',
                    detail: '',
                    latestBid: '',
                    photos: [],
                    description: '',
                    bidHistory:[
                        {
                            time: this.showTime(new Date(2020, 8, 10, 10, 10)) ,
                            user:'UMR',
                            price: '$123123',
                        },
                        {
                            time: this.showTime(new Date(2020, 8, 10, 10, 10)),
                            user:'ooo',
                            price: '$123123',
                        },
                        {
                            time: this.showTime(new Date(2020, 8, 10, 10, 10)),
                            user:'TSF',
                            price: '$123123',
                        },
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
            this.propInfo.bidHistory.push({time:new Date(2009,1,1,1,1,1), user:'aaa', price:'$123123123'});
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
            newBidTip() {
                let tip = "";
                if (!this.newPlacedBid && !this.currentBid) {
                    tip = "0";
                    this.tipError = false;
                } else if(!this.newPlacedBid && this.currentBid){
                    tip = this.currentBid;
                    this.tipError = false;
                }else{
                    tip = this.newPlacedBid;
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
                this.countDown(this.propInfo.enddate,this.propInfo.startdate);
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
                        return 'status-not-start';
                        break;
                    case 'A':
                        return 'status-process';
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
                // let startTime = dayjs(new Date(2020, 8, 10, 10, 10));
                // let expiredTime = dayjs(new Date(2020, 9, 24, 17, 1));
                let nowTime = dayjs();

                let diff = expiredTime.diff(nowTime) / 1000;
                let diff2 = startTime.diff(nowTime) / 1000;
                // console.log(diff);
                // console.log(diff2);

                let day = parseInt(diff / 3600 / 24);
                let hour = parseInt((diff / 3600) % 24);
                let minute = parseInt((diff / 60) % 60);
                let second = parseInt(diff % 60);

                if (diff2 > 0) {
                    this.timeFlag = true;
                    let st = dayjs(startTime).format("YYYY-MM-DD HH:mm:ss");
                    this.time = `This Auction will start at ${ st }`;
                }
                else{
                    if(diff>0){
                        this.time = `Time Left: ${day} Days: ${hour} Hours: ${minute} Mins: ${second} Secs `;
                    }else{
                        this.timeFlag = false;
                        // console.log('over');
                    }

                }
            },
            showTime(time){
                let st = dayjs(time).format("YYYY-MM-DD HH:mm:ss");
                return `${ st }`;
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

            submitCard(){
                if(this.addNewCard){
                    this.$refs["form"].validate((valid) => {
                        if (valid) {
                            let data = new FormData();
                            if(this.addNewCard){
                                data.append('addNewCard', this.addNewCard);
                                data.append('name',this.form.name);
                                data.append('cardNumber', this.form.cardNumber);
                                data.append('expiredDate', this.form.expiredDate);
                                data.append('cvc', this.form.cvc);
                            }else{
                                data.append('addNewCard', this.addNewCard);
                                data.append('cardNumber', this.selectCard);
                            }

                            this.$axios.post('/addcard', data)
                                .then((response) => {
                                    if (response.status >= 200 && response.status < 300) {
                                        if(response.data.code === 200){
                                            this.$message.success("Register successful!");
                                        }
                                    } else if(response.data.code === 400){
                                        this.$message.error(response.msg);
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
                }
                this.addNewCard = false;
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
                this.currentBid = res.currentBid;
                let tabel = [res.user, res.userbid, res.bidtime];
                this.notice(res.user);
                this.history.push(table);


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
        },
        destroyed() {
            this.websock.close() //离开路由之后断开websocket连接
        },
    };
</script>

<style scoped lang="scss">
    .form{
        margin-left:10%;
        margin-top:20px;
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
        box-shadow: 2px 1px 5px 4px #d5dbea;
    }
    .bid {
        width :80%;
        padding: 10px;
        text-align: center;
        font-size: 25px;
        font-weight: bold;
        color: #fff;
        background-color: #133264;
        border-radius: 5px;
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
    .status-process {
        background-color: #89c668;
    }
    /*.status-process {*/
    /*    background-color: #e6a23c;*/
    /*}*/

    .status-not-start {
        background-color: #f56c6c;
    }

</style>
