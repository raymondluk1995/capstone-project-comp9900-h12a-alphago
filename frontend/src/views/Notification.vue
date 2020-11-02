<template>
    <div class="notification">
        <Header>
            <el-button round type="primary" @click="back">Back</el-button>
        </Header>

        <el-row type="flex" justify="center">

            <el-col :span="16" style="box-shadow: 10px 10px 15px -15px rgba(70,92,132,0.45);padding:50px">
                <h5 style="color:rgba(90,104,134,0.6);margin-bottom:50px">
                    <i class="el-icon-document-copy"></i>
                    My Notifications</h5>
                <el-collapse class="notice">
                    <el-collapse-item
                            v-for="(item, index) in Notice"
                            :key="index"
                            :title="getTitle(item)"
                            :name="index"
                            :class="item.read ?  '':'unread' "
                            @click="hasRead(item.read)"
                    >
                        <el-card style="overflow-y: scroll; " :class="item.read ? 'card' : 'card unread'">
                            <div v-if="item.role ==='seller'" style="padding:0 50px;">
<!--                                <p style="font-size: 15px;font-weight:bold">Your property has been sold.</p>-->
                                <h3 style="
                                padding:0 100px;
                                width:100%;
                                font-size: 20px;
                                font-weight:bold;
                               ">{{item.address}}</h3>
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
                                        <el-form-item label="Bid Winner:" prop="bidder">
                                            <span> {{item.username}} </span>
                                        </el-form-item>

                                        <el-form-item label="Highest Bid:" prop="bidder">
                                            <span> {{item.latestPrice |numFormat}} </span>
                                        </el-form-item>

                                    </el-row>
                                    </el-col>
                                    <el-col :span="12" >
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

                                    <el-row>
                                        <el-row>
                                            <h5 style="margin-top:10px" >Winner Details</h5>
                                        </el-row>
                                        <el-col :span="12">
                                            <el-row tyle="flex" justify="left">
                                                <el-form-item label="Bid Winner:" prop="bidder">
                                                    <span> {{item.username}} </span>
                                                </el-form-item>
<!--                                                <el-form-item label="Highest Bid:" prop="bidder">-->
<!--                                                    <span> {{item.latestPrice |numFormat}} </span>-->
<!--                                                </el-form-item>-->

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
                                    border-bottom:2px solid rgba(153,169,191,0.67);">
                                        <h5 style="margin-top:10px">Bid History</h5>
                                    </el-row>

                                    <el-table :data="item.bidHistory"
                                              :max-height="300"
                                              border
                                              stripe
                                              tooltip-effect="light"
                                              style="overflow-y: scroll; "
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

                                </el-form>

                            </div>
                        </el-card>
                    </el-collapse-item>
                </el-collapse>

                <!-- <el-card
                  :class="item.read ? 'card' : 'card unread'"
                  v-for="(item, index) in NotificationList"
                  :key="index"
                >
                  <div @click="read(item)">{{ item.content }}</div>
                </el-card> -->
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
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
                Notice: [
                    {
                        status:'S',
                        role:'seller',
                        title:'',
                        username:'UMR',
                        email:'123@gmnail.com',
                        firstname:'First',
                        lastname:'Last',
                        phone:'0412345678',
                        bidHistory:[
                            {
                                time: 1603981349 ,
                                uid:123,
                                user:'UMR',
                                price: '123123123',
                            },
                            {
                                time: 160398890,
                                user:'ooo',
                                uid:345,
                                price: '1110001',
                            },
                            {
                                time: 1603890349,
                                user:'TSF',
                                uid:456,
                                price: '123123',
                            },


                        ],
                        address:'2 Gearin Alley, Mascot, NSW',
                        read: false,
                        latestPrice:'123000'
                    },
                    {
                        role:'bidder',
                        status:'S',
                        title:'',
                        read: true,
                    },
                    {
                        role:'bidder',
                        status:'S',
                        title:'',
                        read: true,
                    },
                ],
            };
        },
        methods: {
            getTitle(item){

                if(item.role ==='seller' && item.status ==='S' ){
                    return '[Success] Your property has been sold!'
                }else if (item.role ==='seller' && item.status ==='F' ){
                    return '[Failed] Your property has passed in!'
                }else if(item.role==='bidder' && item.status==='S'){
                    return '[Success] You win the property!'
                }


            },
            hasRead(read){
                if(read ===false){

                }
            },

            getStatus(status){
                if(!status){
                    return isread
                }else{
                    return notread;
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
        padding:20px;
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
