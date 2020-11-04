<template>
    <div class="notification">
        <Header>
            <template v-if="this.hasLogin">
                <el-dropdown trigger="hover" @command="handleCommand" style="align-items: center" placement="bottom">
                    <div class="user">
                        <el-badge v-if="parseInt(this.unread) > 0" :value="this.unread" :max="99" class="item">
                            <el-avatar :size="70" :src="avatar"></el-avatar>
                        </el-badge>
                        <el-avatar  v-else :size="70" :src="avatar"></el-avatar>

                    </div>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="profile" icon="el-icon-user-solid"> My Profile</el-dropdown-item>
                        <el-dropdown-item command="property"  icon="el-icon-house"> My Properties</el-dropdown-item>
                        <el-dropdown-item command="auction" icon="el-icon-s-home"> My Auctions</el-dropdown-item>

                        <el-dropdown-item command="notification"  icon="el-icon-bell">
                            Notifications <el-badge v-show="parseInt(this.unread) > 0" class="mark" :value="this.unread" style="padding:0;background-color: white"/>
                        </el-dropdown-item>

                        <el-dropdown-item command="logout" icon="el-icon-turn-off"> Log out</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
            <template v-else >

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

        <el-row type="flex" justify="center" style="background-color: #f1f1f1; min-height: calc(100vh - 170px);">

            <el-col :span="16" style="border-top:40px solid rgba(35,61,112,0.44);box-shadow: 10px 10px 15px -15px rgba(70,92,132,0.45);padding:50px;background-color:white">
                <el-row >
                    <el-col :span="19">
                <h5 style="color:rgba(41,48,62,0.69); margin-bottom:0;padding:0">
                    <i class="el-icon-document-copy"></i>
                    My Notifications</h5>
            </el-col>
                    <el-col :span="5">
                        <el-select
                                v-model="filter"
                                style="margin:0 0 10px 30px"
                                placeholder="Select"
                                @change="changeSearch"
                        >
                            <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                            >
                            </el-option>
                        </el-select>
                    </el-col>
                </el-row>
                <el-collapse class="notice">
                    <div v-for="(item,index) in NoticeShow" @click="hasRead(item)">
                    <el-collapse-item
                            :key="item.notiId"
                            :title="getTitle(item)"
                            :name="index"
                            :class="item.isRead ?  '':'unread' "
                    >
                        <el-card style="overflow-y: scroll; ">
                            <div v-if="item.message.seller && item.message.success" style="padding:0 50px;">
                                <p style="font-size: 18px;font-weight:bold">Dear {{item.message.sellerName}}</p>
                                <p>Congratulations!</p>
                                <p>Your Property<span style="font-size:12px;color:#596c84">[PID:{{item.message.pid}}]</span> has been sold. The highest bid is ${{item.message.bidPrice|numFormat}}.</p>
                                <p style="
                                margin-bottom:50px;">Here is the details. Thank you for using AlphaGo Auction.</p>

                                <span
                                style="

                                width:100%;
                                font-size: 20px;
                                font-weight:bold;
                               ">{{item.message.address}} </span>
                                <span style="font-size:12px;color:#596c84">[AID:{{item.message.aid}}]</span>
                                <el-form
                                        class="form1"
                                        ref="item.message"
                                        :model="item.message"
                                        label-width="100px"
                                        label-position="left"
                                >
                                    <el-row style="
                                    border-top:2px solid rgba(153,169,191,0.67);
                                    border-bottom:2px solid rgba(153,169,191,0.67);">
                                        <el-row>
                                            <h5 style="margin-top:10px" >Seller Details</h5>
                                        </el-row>
                                    <el-col :span="12" >
                                    <el-row tyle="flex" justify="left">
                                        <el-form-item label="Seller:" prop="bidder">
                                            <span> {{item.message.sellerName}} </span>
                                        </el-form-item>

                                    </el-row>
                                    </el-col>
                                    <el-col :span="12" >
                                        <el-form-item label="Name:" prop="bidder">
                                            <span> {{item.message.sellerFullName }} </span>
                                        </el-form-item>
                                        <el-form-item label="Phone:" prop="bidder">
                                            <span> {{item.message.sellerPhone}} </span>
                                        </el-form-item>
                                        <el-form-item label="Email:" prop="bidder">
                                            <span> {{item.message.sellerEmail}} </span>
                                        </el-form-item>

                                    </el-col>
                                    </el-row>

                                    <el-row>
                                        <el-row>
                                            <h5 style="margin-top:10px" >Winner Details</h5>
                                        </el-row>
                                        <el-col :span="12">
                                            <el-row tyle="flex" justify="left">
                                                <el-form-item label="Bidder:" prop="bidder">
                                                    <span> {{item.message.sellerName}} </span>
                                                </el-form-item>
                                                <el-form-item label="Highest Bid:" prop="bidder">
                                                    <span> $ {{item.message.bidPrice |numFormat}} </span>
                                                </el-form-item>

                                            </el-row>
                                        </el-col>
                                        <el-col :span="12">

                                            <el-form-item label="Name:" prop="bidder">
                                                <span> {{item.message.bidderFullName }} </span>
                                            </el-form-item>
                                            <el-form-item label="Phone:" prop="bidder">
                                                <span> {{item.message.bidderPhone}} </span>
                                            </el-form-item>
                                            <el-form-item label="Email:" prop="bidder">
                                                <span> {{item.message.bidderEmail}} </span>
                                            </el-form-item>
                                        </el-col>
                                    </el-row>

                                    <el-row style="
                                    border-top:2px solid rgba(153,169,191,0.67);
                                   ">
                                        <h5 style="margin-top:10px">Bid History</h5>
                                        <el-table :data="item.history"
                                                  border
                                                  stripe
                                                  tooltip-effect="light"
                                                  :default-sort = "{prop: 'price', order: 'descending'}"
                                        >

                                            <template v-for="(i, index) in columns">
                                                <el-table-column
                                                        :key="index"
                                                        :prop="i.prop"
                                                        :label="i.label"
                                                        :formatter="i.formatter"
                                                >
                                                </el-table-column>
                                            </template>
                                        </el-table>
                                    </el-row>



                                </el-form>

                            </div>

                            <div v-if="item.message.seller && !item.message.success" style="padding:0 50px;">
                                <p style="font-size: 18px;font-weight:bold">Dear {{item.message.sellerName}}</p>
                                <p>Sorry.. </p>
                                <p v-if="item.message.bidPrice!==''">Your Property<span style="font-size:12px;color:#596c84">[PID:{{item.message.pid}}]</span> has passed in. The highest bid is ${{item.message.bidPrice| numFormat }}.</p>
                                <p v-else>Your Property<span style="font-size:12px;color:#596c84">[PID:{{item.message.pid}}]</span> has passed in. No bidders have participated in this auction.</p>

                                <p style="
                                margin-bottom:50px;">Here is the details. Thank you for using AlphaGo Auction.</p>

                                <span
                                        style="

                                width:100%;
                                font-size: 20px;
                                font-weight:bold;
                               ">{{item.message.address}}</span>
                                <span style="font-size:12px;color:#596c84">[AID:{{item.message.aid}}]</span>

                                <el-form
                                        class="form1"
                                        ref="form2"
                                        :model="form2"
                                        :rules="rules"
                                        label-width="100px"
                                        label-position="left"
                                >
                                    <el-row style="
                                    border-top:2px solid rgba(153,169,191,0.67);
                                    border-bottom:2px solid rgba(153,169,191,0.67);">
                                        <el-row>
                                            <h5 style="margin-top:10px" >Seller Details</h5>
                                        </el-row>
                                        <el-col :span="12" >
                                            <el-row tyle="flex" justify="left">
                                                <el-form-item label="Seller:" prop="bidder">
                                                    <span> {{item.message.sellerName}} </span>
                                                </el-form-item>

                                            </el-row>
                                        </el-col>
                                        <el-col :span="12" >
                                            <el-form-item label="Name:" prop="bidder">
                                                <span> {{item.message.sellerFullName }} </span>
                                            </el-form-item>
                                            <el-form-item label="Phone:" prop="bidder">
                                                <span> {{item.message.sellerPhone}} </span>
                                            </el-form-item>
                                            <el-form-item label="Email:" prop="bidder">
                                                <span> {{item.message.sellerEmail}} </span>
                                            </el-form-item>

                                        </el-col>
                                    </el-row>

                                    <el-row style="
                                    border-top:2px solid rgba(153,169,191,0.67);
                                   ">
                                        <h5 style="margin-top:10px">Bid History</h5>
                                        <el-table :data="item.message.history"
                                                  :max-height="300"
                                                  border
                                                  stripe
                                                  tooltip-effect="light"
                                                  style="overflow-y: scroll;"
                                                  :default-sort = "{prop: 'price', order: 'descending'}"
                                        >

                                            <template v-for="(i, index) in columns">
                                                <el-table-column
                                                        :key="index"
                                                        :prop="i.prop"
                                                        :label="i.label"
                                                        :formatter="i.formatter"
                                                >
                                                </el-table-column>
                                            </template>
                                        </el-table>
                                    </el-row>



                                </el-form>

                            </div>

                            <div v-if="!item.message.seller && item.message.success" style="padding:0 50px;">
                                <p style="font-size: 18px;font-weight:bold">Dear {{item.message.sellerName}}</p>
                                <p>Congratulations!</p>
                                <p>Your win the Property<span style="font-size:12px;color:#596c84">[PID:{{item.message.pid}}]</span>.
                                 Your highest bid is ${{item.message.bidPrice|numFormat}}.</p>
                                <p style="
                                margin-bottom:50px;">Here is the details. Thank you for using AlphaGo Auction.</p>

                                <span
                                        style="

                                width:100%;
                                font-size: 20px;
                                font-weight:bold;
                               ">{{item.message.address}}</span>
                                <span style="font-size:12px;color:#596c84">[AID:{{item.message.aid}}]</span>
                                <el-form
                                        class="form1"
                                        ref="item.message"
                                        :model="item.message"
                                        label-width="100px"
                                        label-position="left"
                                >
                                    <el-row style="
                                    border-top:2px solid rgba(153,169,191,0.67);
                                    border-bottom:2px solid rgba(153,169,191,0.67);">
                                        <el-row>
                                            <h5 style="margin-top:10px" >Seller Details</h5>
                                        </el-row>
                                        <el-col :span="12" >
                                            <el-row tyle="flex" justify="left">
                                                <el-form-item label="Seller:" prop="bidder">
                                                    <span> {{item.message.sellerName}} </span>
                                                </el-form-item>

                                            </el-row>
                                        </el-col>
                                        <el-col :span="12" >
                                            <el-form-item label="Name:" prop="bidder">
                                                <span> {{item.message.sellerFullName }} </span>
                                            </el-form-item>
                                            <el-form-item label="Phone:" prop="bidder">
                                                <span> {{item.message.sellerPhone}} </span>
                                            </el-form-item>
                                            <el-form-item label="Email:" prop="bidder">
                                                <span> {{item.message.sellerEmail}} </span>
                                            </el-form-item>

                                        </el-col>
                                    </el-row>

                                    <el-row>
                                        <el-row>
                                            <h5 style="margin-top:10px" >Winner Details</h5>
                                        </el-row>
                                        <el-col :span="12">
                                            <el-row tyle="flex" justify="left">
                                                <el-form-item label="Bidder:" prop="bidder">
                                                    <span> {{item.message.username}} </span>
                                                </el-form-item>
                                                <el-form-item label="Highest Bid:" prop="bidder">
                                                    <span>$ {{item.message.bidPrice |numFormat}} </span>
                                                </el-form-item>

                                            </el-row>
                                        </el-col>
                                        <el-col :span="12">

                                            <el-form-item label="Name:" prop="bidder">
                                                <span> {{item.message.bidderFullName }}</span>
                                            </el-form-item>
                                            <el-form-item label="Phone:" prop="bidder">
                                                <span> {{item.message.bidderPhone}} </span>
                                            </el-form-item>
                                            <el-form-item label="Email:" prop="bidder">
                                                <span> {{item.message.bidderEmail}} </span>
                                            </el-form-item>
                                        </el-col>
                                    </el-row>

                                </el-form>

                            </div>
                        </el-card>
                    </el-collapse-item>
                    </div>
                </el-collapse>

            </el-col>
        </el-row>
    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
    import dayjs from "dayjs";
    import $ from 'jquery'
    import { mapActions } from "vuex";


    export default {
        name: "Notification",
        components: {
            Header,
        },
        data() {
            return {
                columns: [
                    {prop: 'time', label: 'Time',width: '300',formatter: this.showTime_table},
                    {prop: 'uid', label: 'UID', width: '300'},
                    {prop: 'username', label: 'User', width: '300'},
                    {prop: 'price', label: 'Current Bid',formatter: this.formatPrice,sortable:true}
                ],
                unread:0,
                avatar:'',
                options: [
                    {
                        value: "all",
                        label: "All",
                    },
                    {
                        value: true,
                        label: "Read",
                    },
                    {
                        value: false,
                        label: "Unread",
                    },
                ],
                filter: "all",
                hasLogin:false,
                Notice: [
                    {
                        createTime:new Date(2020,10,10,10,10),
                        noti_id:'12',
                        noti_type:'FINISH',
                        isRead:false,
                        uid:23,
                        message: {
                            aid:'0',
                            pid: '123',
                            success: true,
                            seller: true,
                            bidPrice:'1230000',
                            address: '2 Gearin Alley, Mascot, NSW',

                            sellerName: 'UMR',
                            sellerFullName: 'Tony Stark',
                            sellerPhone:'0123123',
                            sellerEmail: '123@gmnail.com',

                            bidderName: 'TSF',
                            bidderFullName: 'First Last',
                            bidderPhone: '0412345678',
                            bidderEmail:'123123123.com',

                            history: [
                                {
                                    time: 1603981349,
                                    uid: 123,
                                    user: 'UMR',
                                    price: '123123123',
                                },
                                {
                                    time: 160398890,
                                    user: 'ooo',
                                    uid: 345,
                                    price: '1110001',
                                },
                                {
                                    time: 1603890349,
                                    user: 'TSF',
                                    uid: 456,
                                    price: '123123',
                                },
                            ],

                        }
                    },
                    {
                        createTime:new Date(2020,10,10,10,10),
                        noti_id:'12',
                        noti_type:'FINISH',
                        isRead:true,
                        uid:23,
                        message: {
                            aid:'0',
                            pid: '123',
                            success: true,
                            seller: true,
                            bidPrice:'1230000',
                            address: '2 Gearin Alley, Mascot, NSW',

                            sellerName: 'UMR',
                            sellerFullName: 'Tony Stark',
                            sellerPhone:'0123123',
                            sellerEmail: '123@gmnail.com',

                            bidderName: 'TSF',
                            bidderFullName: 'First Last',
                            bidderPhone: '0412345678',
                            bidderEmail:'123123123.com',

                            history: [
                                {
                                    time: 1603981349,
                                    uid: 123,
                                    user: 'UMR',
                                    price: '123123123',
                                },
                                {
                                    time: 160398890,
                                    user: 'ooo',
                                    uid: 345,
                                    price: '1110001',
                                },
                                {
                                    time: 1603890349,
                                    user: 'TSF',
                                    uid: 456,
                                    price: '123123',
                                },
                            ],

                        }
                    },
                ],
                NoticeShow:[]
            };
        },

        created() {
            this.username = localStorage.getItem("username");
            // this.username= '123';

            if(this.username!==null){
                this.hasLogin = true;
                this.avatar = localStorage.getItem('avatar');
                this.$axios
                    .get('/notification/unread')
                    .then(response => {
                        if (response.data.code === 200) {
                            this.unread = parseInt(response.data.result);
                        }
                    })
                    .catch(function (error) {
                        this.$message.error(error);
                    });
                this.$axios
                    .get('/notification/all')
                    .then(response => {
                        if (response.data.code === 200) {
                            this.Notice = response.data.result;
                            this.NoticeShow = response.data.result;
                        }
                    })
                    .catch(function (error) {
                        this.$message.error(error);
                    });
            }
            else{
                this.$message.error("You should login first!");
                // this.$router.push("/login");
            }

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
                        this.$router.push("/notice");
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
            changeSearch(value) {
                let filterNotice = [];
                if(value === 'all'){
                    filterNotice = this.Notice;
                }else{
                    filterNotice = this.Notice.filter((e) => {
                        return e.isRead === value;
                    });
                }
                this.NoticeShow = filterNotice;
            },

            getTitle(item){
                let time = this.showTime(item.createTime);
                // console.log(time)
                if(item.message.seller && item.message.success){
                    return `[Success] Your property has been sold!    [${ time }]`;
                }else if (item.message.seller && !item.message.success){
                    return `[Failed] Your property has passed in! [${ time }]`
                }else if(!item.message.seller && item.message.success){
                    return `[Success] You win the property! [${ time }]`
                }
            },
            formatPrice(row,coloum) {
                // console.log(number)
                let temp =  row.price.toString().replace(/(\d)(?=(?:\d{3})+$)/g,'$1,')
                return `$ ${ temp }`;
            },
            showTime_table(row,column){
                // console.log(row.time)
                let st = dayjs(row.time).format("YYYY-MM-DD HH:mm:ss");
                return `${ st }`;
            },

            showTime(time){
                let st = dayjs(time).format("YYYY-MM-DD HH:mm:ss");
                return st;
            },

            hasRead(item){
                if(item.isRead ===false){
                    let data = new FormData;
                    data.append('notiId', item.notiId);
                    this.$axios.post('/notification/isRead',data)
                        .then((response) => {
                            if (response.data.code === 200) {
                                this.unread = this.unread-1;
                                item.isRead = true;
                            }
                    })
                        .catch((res) => {
                            this.$message.error('Error');
                        });
                    // this.unread = this.unread-1;
                }
            },


            read(item) {
                item.isRead = true;
            },
            back() {
                this.$router.go(-1);
            },
        },
    };
</script>

<style lang="scss">
    .form1{
        /*padding:20px;*/
        .el-form-item{
            margin-bottom:0;
        }
    }
    .card {
        margin: 20px;
        height:500px;
        overflow-y: scroll;
        /*&:hover {*/
        /*    cursor: pointer;*/
        /*    transform: scale(1.02);*/
        /*}*/
        h3,
        p {
            line-height: 2.5;
        }
    }
    /*.unread {*/
    /*    background-color: #ccc;*/
    /*}*/
    .el-collapse-item__header{
        font-size:15px !important;
        padding: 20px 20px !important;
        height:80px !important;

    }

    .unread {
        .el-collapse-item__header {
            font-weight:bold !important;
        }
    }



</style>
