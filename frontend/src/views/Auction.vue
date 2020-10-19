<template>
    <div class="auction">
        <Header>
            <template v-if="this.hasLogin">
                <el-dropdown trigger="click" @command="handleCommand" style="align-items: center" placement="bottom">
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
            <el-col :span="20">
                <el-tabs type="border-card">
                    <el-tab-pane label="My Properties">
                        <div style="text-align:right">
                            <el-button type="primary" round @click="goto('propreg')">Register new Property</el-button>
                        </div>
                        <template v-if="!isEmpty">
                        <el-card class="card" v-for="item in propList" :key="item.id" @click.native="goDetails(item)">
                            <el-card :body-style="{ padding: '0px' }" style="height: 600px">
                                <el-carousel :interval="5000" arrow="always" :height="cheight">
                                    <el-carousel-item v-for="pic in item.photos" :key="pic.id">
<!--                                        <h3>{{ pic }}</h3>-->
                                        <img :src="pic"  width="100%" height="100%" alt=""/>
                                    </el-carousel-item>
                                </el-carousel>
                                <div style="padding: 14px;">
                                    <h6>{{ item.address }}</h6>
                                    <el-row type="flex" justify="left" style="margin:10px 5%;">
                                        <el-col :span="6">
                                        <i class="el-icon-toilet-paper"> Bathrooms: {{ item.bathroomNum}}</i>
                                        </el-col>
                                        <el-col :span="6">
                                        <i class="el-icon-house"> Bedrooms: {{ item.bedroomNum }}</i>
                                        </el-col>
                                            <el-col :span="6">
                                        <i class="el-icon-truck"> Garages: {{ item.garageNum }}</i>
                                            </el-col>
                                        <el-col :span="6">
                                            <i class="el-icon-full-screen"> Area: {{ item.area }}</i>
                                        </el-col>
                                        <el-col :span="8">
                                            <i class="el-icon-info"> Type: {{ item.type }}</i>
                                        </el-col>
                                    </el-row>
                                </div>
                            </el-card>
                        </el-card>
                        </template>
                        <template v-else>
                            <div class="empty-label" >
                                <el-alert
                                        title="You haven't post any property."
                                        type="info"
                                        center
                                        show-icon
                                        :closable="false">
                                </el-alert>
                            </div>
                        </template>
                    </el-tab-pane>
                    <el-tab-pane label="As a Bidder">
                        <p>This area is under construction... ...</p>
                        <p>Please come back later!</p>
                    </el-tab-pane>
                    <el-tab-pane label="As a Seller">
                        <p>This area is under construction... ...</p>
                        <p>Please come back later!</p>
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
        props: {
            cheight: {
                type: String,
                default: '500px'
            }
        },
        components: {
            Header,
        },
        created () {
            // this.username = localStorage.getItem('username');
            // if(this.username!==null){
            //     this.hasLogin = true;
            //     this.avatar = localStorage.getItem('avatar');
            //     this.firstname=  localStorage.getItem('firstname');
            // }else{
            //     this.$message.error("You should login first!");
            //     this.$router.push("/login");
            // }

            this.$axios
                .get('/property/information')
                .then(response => {
                    if (response.data.code === 200) {
                        this.isEmpty=false;
                        this.propList = response.data.result;
                    }
                    if (response.data.code === 401){
                        this.$message.error('You should login first!');
                        this.$router.replace("/login");
                    }
                })
                .catch(function (error) {
                    console.log(error)
                })

        },
        data() {
            return {
                isEmpty: true,
                hasLogin: false,
                propList: [],
            }
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
                    case "property":
                        this.$router.push("/propmag");
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
            goto(name) {
                console.log(name);
                this.$router.push({ name: name });
            },
            back() {
                this.$router.go(-1);
            },
            goDetails (item) {
                this.$router.push(
                    {
                        path: '/property',
                        query:
                            {
                                id: item.id,
                            }
                    }
                )
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
.card {
    margin: 20px 20%;
    height: 650px;

    &:hover {
        cursor: pointer;
        transform: scale(1.02);
    }

    h6{
        margin:5px 5%;
    }
    p {
        line-height: 2.5;
    }
    border-top: 15px solid #bcc8e6;
}
.empty-label{
    /*font-size: 18px;*/
    text-align: center;
    margin: 10px;
}

.img{
    width: auto;
    height: auto;
    max-width: 100%;
    max-height: 100%;
}


</style>
