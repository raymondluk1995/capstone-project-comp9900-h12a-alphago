<template>
    <div class="notification">
        <Header>
            <div class="back-btn">
                <span  id="back-btn" style="padding:2px 5px;font-size:20px;" @click="back">Back <i class="el-icon-refresh-right"></i></span>
                <div class="bottom-line"></div>
            </div>
        </Header>

        <el-row type="flex" justify="center" style="background-color: #f1f1f1; min-height: calc(100vh - 170px);">

            <el-col :span="16" style="border-top:40px solid rgba(35,61,112,0.44);box-shadow: 10px 10px 15px -15px rgba(70,92,132,0.45);padding:50px;background-color:white">
                <el-row >
                <h5 style="color:rgba(41,48,62,0.69);margin-bottom:50px">
                    <i class="el-icon-document-copy"></i>
                    My Notifications</h5>
                </el-row>
                <el-collapse class="notice">
                    <div v-for="(item,index) in Notice" @click="hasRead(item)">
                    <el-collapse-item
                            :key="index"
                            :title="getTitle(item)"
                            :name="index"
                            :class="item.read ?  '':'unread' "
                    >
                        <el-card style="overflow-y: scroll; " :class="item.read ? 'card' : 'card unread'">
                            <div v-if="item.role ==='seller' && item.status ==='S'" style="padding:0 50px;">
                                <p style="font-size: 18px;font-weight:bold">Dear {{item.sellername}}</p>
                                <h6>Congratulations!</h6>
                                <h6>Your Property[{{item.pid}}] has been sold. The highest bid is ${{item.latestPrice|numFormat}}.</h6>
                                <h6 style="
                                margin-bottom:50px;">Here is the details. Thank you for using AlphaGo Auction.</h6>

                                <span
                                style="

                                width:100%;
                                font-size: 20px;
                                font-weight:bold;
                               ">{{item.address}}</span>
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
                                            <span> {{item.sellername}} </span>
                                        </el-form-item>

                                    </el-row>
                                    </el-col>
                                    <el-col :span="12" >
                                        <el-form-item label="Seller Name:" prop="bidder">
                                            <span> {{item.sellerfirstname }} {{item.sellerlastname}} </span>
                                        </el-form-item>
                                        <el-form-item label="Phone:" prop="bidder">
                                            <span> {{item.sellerphonne}} </span>
                                        </el-form-item>
                                        <el-form-item label="Email:" prop="bidder">
                                            <span> {{item.selleremail}} </span>
                                        </el-form-item>

                                    </el-col>
                                    </el-row>

                                    <el-row>
                                        <el-row>
                                            <h5 style="margin-top:10px" >Winner Details</h5>
                                        </el-row>
                                        <el-col :span="12">
                                            <el-row tyle="flex" justify="left">
                                                <el-form-item label="Bid Winner:" prop="bidder">
                                                    <span> {{item.username}} </span>
                                                </el-form-item>
                                                <el-form-item label="Highest Bid:" prop="bidder">
                                                    <span> {{item.latestPrice |numFormat}} </span>
                                                </el-form-item>

                                            </el-row>
                                        </el-col>
                                        <el-col :span="12">

                                            <el-form-item label="Bider Name:" prop="bidder">
                                                <span> {{item.firstname }} {{item.lastname}} </span>
                                            </el-form-item>
                                            <el-form-item label="Phone:" prop="bidder">
                                                <span> {{item.phone}} </span>
                                            </el-form-item>
                                            <el-form-item label="Email:" prop="bidder">
                                                <span> {{item.email}} </span>
                                            </el-form-item>
                                        </el-col>
                                    </el-row>

                                    <el-row style="
                                    border-top:2px solid rgba(153,169,191,0.67);
                                   ">
                                        <h5 style="margin-top:10px">Bid History</h5>
                                        <el-table :data="item.bidHistory"
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

                            <div v-if="item.role ==='seller' && item.status ==='F'" style="padding:0 50px;">
                                <p style="font-size: 18px;font-weight:bold">Dear {{item.sellername}}</p>
                                <h6>Sorry.. </h6>
                                <h6>Your Property[{{item.pid}}] has passed in. The highest bid is ${{item.latestPrice| numFormat }}.</h6>
                                <h6 style="
                                margin-bottom:50px;">Here is the details. Thank you for using AlphaGo Auction.</h6>

                                <span
                                        style="

                                width:100%;
                                font-size: 20px;
                                font-weight:bold;
                               ">{{item.address}}</span>
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
                                                    <span> {{item.username}} </span>
                                                </el-form-item>

                                            </el-row>
                                        </el-col>
                                        <el-col :span="12" >
                                            <el-form-item label="Seller Name:" prop="bidder">
                                                <span> {{item.sellerfirstname }} {{item.sellerlastname}} </span>
                                            </el-form-item>
                                            <el-form-item label="Phone:" prop="bidder">
                                                <span> {{item.sellerphonne}} </span>
                                            </el-form-item>
                                            <el-form-item label="Email:" prop="bidder">
                                                <span> {{item.selleremail}} </span>
                                            </el-form-item>

                                        </el-col>
                                    </el-row>

                                    <el-row style="
                                    border-top:2px solid rgba(153,169,191,0.67);
                                   ">
                                        <h5 style="margin-top:10px">Bid History</h5>
                                        <el-table :data="item.bidHistory"
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

                            <div v-if="item.role ==='bidder' && item.status ==='S'" style="padding:0 50px;">
                                <p style="font-size: 18px;font-weight:bold">Dear {{item.sellername}}</p>
                                <h6>Congratulations!</h6>
                                <h6>Your win the Property[{{item.pid}}].
                                 Your highest bid is ${{item.latestPrice|numFormat}}.</h6>
                                <h6 style="
                                margin-bottom:50px;">Here is the details. Thank you for using AlphaGo Auction.</h6>

                                <span
                                        style="

                                width:100%;
                                font-size: 20px;
                                font-weight:bold;
                               ">{{item.address}}</span>
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
                                                    <span> {{item.sellername}} </span>
                                                </el-form-item>

                                            </el-row>
                                        </el-col>
                                        <el-col :span="12" >
                                            <el-form-item label="Seller Name:" prop="bidder">
                                                <span> {{item.sellerfirstname }} {{item.sellerlastname}} </span>
                                            </el-form-item>
                                            <el-form-item label="Phone:" prop="bidder">
                                                <span> {{item.sellerphonne}} </span>
                                            </el-form-item>
                                            <el-form-item label="Email:" prop="bidder">
                                                <span> {{item.selleremail}} </span>
                                            </el-form-item>

                                        </el-col>
                                    </el-row>

                                    <el-row>
                                        <el-row>
                                            <h5 style="margin-top:10px" >Winner Details</h5>
                                        </el-row>
                                        <el-col :span="12">
                                            <el-row tyle="flex" justify="left">
                                                <el-form-item label="Bid Winner:" prop="bidder">
                                                    <span> {{item.username}} </span>
                                                </el-form-item>
                                                <el-form-item label="Highest Bid:" prop="bidder">
                                                    <span> {{item.latestPrice |numFormat}} </span>
                                                </el-form-item>

                                            </el-row>
                                        </el-col>
                                        <el-col :span="12">

                                            <el-form-item label="Bider Name:" prop="bidder">
                                                <span> {{item.firstname }} {{item.lastname}} </span>
                                            </el-form-item>
                                            <el-form-item label="Phone:" prop="bidder">
                                                <span> {{item.phone}} </span>
                                            </el-form-item>
                                            <el-form-item label="Email:" prop="bidder">
                                                <span> {{item.email}} </span>
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
                unreadNum :'',
                Notice: [
                    // {
                    //     pid:'123',
                    //     id:234,
                    //     status:'F',
                    //     role:'seller',
                    //     title:'',
                    //     username:'UMR',
                    //     sellername:'TSF',
                    //     sellerfirstname:'Tony',
                    //     sellerlastname:'Stark',
                    //     time:new Date(2020,10,10,10,10),
                    //     email:'123@gmnail.com',
                    //     firstname:'First',
                    //     lastname:'Last',
                    //     phone:'0412345678',
                    //     bidHistory:[
                    //         {
                    //             time: 1603981349 ,
                    //             uid:123,
                    //             user:'UMR',
                    //             price: '123123123',
                    //         },
                    //         {
                    //             time: 160398890,
                    //             user:'ooo',
                    //             uid:345,
                    //             price: '1110001',
                    //         },
                    //         {
                    //             time: 1603890349,
                    //             user:'TSF',
                    //             uid:456,
                    //             price: '123123',
                    //         },
                    //
                    //
                    //     ],
                    //     address:'2 Gearin Alley, Mascot, NSW',
                    //     read: false,
                    //     latestPrice:'123000'
                    // },
                    // {
                    //     pid:'123',
                    //     id:234,
                    //     status:'S',
                    //     role:'seller',
                    //     title:'',
                    //     username:'UMR',
                    //     sellername:'TSF',
                    //     sellerfirstname:'Tony',
                    //     sellerlastname:'Stark',
                    //     time:new Date(2020,10,10,10,10),
                    //     email:'123@gmnail.com',
                    //     firstname:'First',
                    //     lastname:'Last',
                    //     phone:'0412345678',
                    //     bidHistory:[
                    //         {
                    //             time: 1603981349 ,
                    //             uid:123,
                    //             user:'UMR',
                    //             price: '123123123',
                    //         },
                    //         {
                    //             time: 160398890,
                    //             user:'ooo',
                    //             uid:345,
                    //             price: '1110001',
                    //         },
                    //         {
                    //             time: 1603890349,
                    //             user:'TSF',
                    //             uid:456,
                    //             price: '123123',
                    //         },
                    //
                    //
                    //     ],
                    //     address:'2 Gearin Alley, Mascot, NSW',
                    //     read: false,
                    //     latestPrice:'123000'
                    // },
                    // {
                    //     pid:'123',
                    //     id:234,
                    //     status:'S',
                    //     role:'bidder',
                    //     title:'',
                    //     username:'UMR',
                    //     sellername:'TSF',
                    //     sellerfirstname:'Tony',
                    //     sellerlastname:'Stark',
                    //     time:new Date(2020,10,10,10,10),
                    //     email:'123@gmnail.com',
                    //     firstname:'First',
                    //     lastname:'Last',
                    //     phone:'0412345678',
                    //     bidHistory:[
                    //         {
                    //             time: 1603981349 ,
                    //             uid:123,
                    //             user:'UMR',
                    //             price: '123123123',
                    //         },
                    //         {
                    //             time: 160398890,
                    //             user:'ooo',
                    //             uid:345,
                    //             price: '1110001',
                    //         },
                    //         {
                    //             time: 1603890349,
                    //             user:'TSF',
                    //             uid:456,
                    //             price: '123123',
                    //         },
                    //
                    //
                    //     ],
                    //     address:'2 Gearin Alley, Mascot, NSW',
                    //     read: false,
                    //     latestPrice:'123000'
                    // },
                    // {
                    //     role:'bidder',
                    //     status:'S',
                    //     title:'',
                    //     read: true,
                    // },
                    // {
                    //     role:'bidder',
                    //     status:'S',
                    //     title:'',
                    //     read: true,
                    // },
                ],
            };
        },
        created() {
            this.username = localStorage.getItem("username");
            // this.username= '123';

            this.$axios
                .get('/notice')
                .then(response => {

                        if (response.data.code === 200) {
                            this.unreadNum = response.data.unread;
                            this.Notice = response.data.notice;
                    }
                })
                .catch(function (error) {
                    this.$message.error(error);
                });


            // this.notFound = true;
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
            getTitle(item){
                let time = this.showTime(item.time);
                console.log(time)
                if(item.role ==='seller' && item.status ==='S' ){
                    return `[Success] Your property has been sold!    [${ time }]`;
                }else if (item.role ==='seller' && item.status ==='F' ){
                    return `[Failed] Your property has passed in! [${ time }]`
                }else if(item.role==='bidder' && item.status==='S'){
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
                if(item.read ===false){
                    let data = new FormData;
                    data.append('nid', item.id);
                    this.$axios.post('/notice/hasread',data)
                        .then((response) => {
                            if (response.data.code === 200) {}
                    })
                        .catch((res) => {
                            this.$message.error('Error');
                        });

                }
            },


            read(item) {
                item.read = true;
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
        height:400px;
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
