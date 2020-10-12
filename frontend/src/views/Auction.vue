<template>
    <div class="auction">
        <Header>
            <template v-if="this.hasLogin">
                <el-dropdown trigger="click" @command="handleCommand">
                    <div class="user">
                        <el-avatar :size="50" :src="avatar"></el-avatar>
                        <p>welcome {{ firstname }}</p>
                    </div>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="profile">My profile</el-dropdown-item>
                        <el-dropdown-item command="auction">My Auctions</el-dropdown-item>
                        <el-dropdown-item command="notification">Notifications</el-dropdown-item>
                        <el-dropdown-item command="logout">Log out</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
            <template v-else>
                <el-button round @click="goto('login')">SIGN IN</el-button>
                <el-button round type="primary" @click="goto('register')">SIGN UP</el-button>
            </template>
        </Header>

        <el-row type="flex" justify="center">
            <el-col :span="20">
                <el-tabs type="border-card">
                    <el-tab-pane label="As a Bidder">
                        <p>This area is under construction... ...</p>
                        <p>Please come back later!</p>
                    </el-tab-pane>
                    <el-tab-pane label="As a Seller">
                        <div style="text-align:right">
                            <el-button type="primary" @click="goto('propreg')">Register new auction</el-button>
                        </div>
                        <el-card
                                class="card"
                                :class="addStatusColor(item.status)"
                                v-for="item in sellList"
                                :key="item.id"
                        >
                            <el-row>
                                <el-col :span="6">
                                    <img :src="item.image" @click="goto('property')" alt="" />
                                </el-col>
                                <el-col :span="14" :offset="1">
                                    <div @click="goto('property')">
                                        <h3>{{ item.title }}</h3>
                                        <el-row type="flex" justify="center" style="margin: 40px 0">
                                            <el-col>
                                                Bathrooms: {{ item.bathNum}}
                                            </el-col>
                                            <el-col>
                                                Bedrooms: {{ item.bedNum }}
                                            </el-col>
                                            <el-col>
                                                Garages: {{ item.carNum }}
                                            </el-col>
                                        </el-row>
                                        <el-row>
                                            Start date: {{ item.startdate }}
                                        </el-row>
                                        <el-row>
                                            End date: {{ item.enddate }}
                                        </el-row>
                                    </div>
                                </el-col>
                            </el-row>
                        </el-card>
                    </el-tab-pane>
                </el-tabs>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
    import { mapActions } from "vuex";
    export default {
        name: "Auction",
        components: {
            Header,
        },
        created () {
            this.username = localStorage.getItem('username');
            if(this.username!==null){
                this.hasLogin = true;
                this.avatar = localStorage.getItem('avatar');
            }
            this.firstname=  localStorage.getItem('firstname');
        },
        data() {
            return {
                status:'',
                hasLogin:"",
                sellList: [
                    {
                        id: 1,
                        status: "0",
                        title: "256 Gearin Alley,Mascot,Nsw",
                        image: "",
                        price: "$145,0000",
                        newBid: "",
                    },
                    {
                        id: 0,
                        status: "1",
                        title: "256 Gearin Alley,Mascot,Nsw",
                        image: "",
                        price: "$145,0000",
                        newBid: "",
                    },
                    {
                        id: -1,
                        status: "-1",
                        title: "",
                        image: "",
                        price: "$145,0000",
                        newBid: "",
                    },
                    {
                        id: -1,
                        status: "-1",
                        title: "",
                        image: "",
                        price: "$145,0000",
                        newBid: "",
                    },
                ],
            };
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
                                        location.reload()
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
            addStatusColor(status) {
                const colors = new Map([
                    ["1", "status-success"],
                    ["0", "status-process"],
                    ["-1", "status-fail"],
                ]);
                return colors.get(status);
            },
            goto(name) {
                console.log(name);
                this.$router.push({ name: name });
            },
            back() {
                this.$router.go(-1);
            },
        },
    };
</script>

<style scoped lang="scss">
    .auction{
        /*margin-top: 5px;*/
    }
    .user {
        display: flex;
        align-items: center;
        p {
            margin-left: 10px;
        }
    }
    .card {
        margin: 20px 0;
        &:hover {
            cursor: pointer;
            transform: scale(1.02);
        }
        h3,
        p {
            line-height: 2.5;
        }
        .price {
            margin: 5px 0;
            color: #fff;
            font-size: 16px;
        }
    }
    .status-success {
        border-left: 15px solid #67c23a;
    }
    .status-process {
        border-left: 15px solid #e6a23c;
    }
    .status-fail {
        border-left: 15px solid #f56c6c;
    }
    .new-bid-wrap {
        display: flex;
        align-items: center;
        .el-icon-circle-plus-outline {
            margin-left: 20px;
            font-size: 30px;
        }
    }
</style>
