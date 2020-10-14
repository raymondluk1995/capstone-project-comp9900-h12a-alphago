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
                        <el-card class="card" v-for="item in propList" :key="item.id">
                            <el-card :body-style="{ padding: '0px' }">
                                <el-carousel :interval="5000" arrow="always">
                                    <el-carousel-item v-for="pic in item.photos" :key="item.id">
<!--                                        <h3>{{ pic }}</h3>-->
                                        <img :src="pic"   alt=""/>
                                    </el-carousel-item>
                                </el-carousel>
                                <div style="padding: 14px;">
                                    <h3>{{ item.address }}</h3>
                                    <el-row type="flex" justify="left" style="margin:10px 5%;">
                                        <el-col :span="5">
                                        <i class="el-icon-toilet-paper"> Bathrooms: {{ item.bathroomNum}}</i>
                                        </el-col>
                                        <el-col :span="5">
                                        <i class="el-icon-house"> Bedrooms: {{ item.bedroomNum }}</i>
                                        </el-col>
                                            <el-col :span="5">
                                        <i class="el-icon-truck"> Garages: {{ item.garageNum }}</i>
                                            </el-col>
                                    </el-row>
                                    <el-row type="flex" justify="left" style="margin:10px 5%;">
                                    <el-col :span="5">
                                        <i class="el-icon-info"> Type: {{ item.type }}</i>
                                    </el-col>
                                    <el-col :span="5">
                                        <i class="el-icon-zoom-in"> Area: {{ item.area }}</i>
                                    </el-col>
                                    </el-row>
                                </div>
                            </el-card>
<!--                            <el-row>-->
<!--                                <el-col :span="6">-->
<!--                                    <img height="200" width="300"  :src="item.image" @click="goto('property')" alt="" />-->
<!--                                </el-col>-->
<!--                                <el-col :span="10" :offset="1">-->
<!--                                    <div @click="goto('property')">-->
<!--                                        <h3>{{ item.address }}</h3>-->
<!--                                        <el-row type="flex" justify="center" style="margin: 40px 0">-->
<!--                                            <el-col>-->
<!--                                                <i class="el-icon-toilet-paper"> Bathrooms: {{ item.bathroomNum}}</i>-->
<!--                                            </el-col>-->
<!--                                            <el-col>-->
<!--                                                <i class="el-icon-house"> Bedrooms: {{ item.bedroomNum }}</i>-->
<!--                                            </el-col>-->
<!--                                            <el-col>-->
<!--                                                <i class="el-icon-truck"> Garages: {{ item.garageNum }}</i>-->
<!--                                            </el-col>-->
<!--                                        </el-row>-->
<!--                                        <el-row type="flex" justify="center" style="margin: 20px 0">-->
<!--                                        <el-col>-->
<!--                                            <i class="el-icon-info"> Type: {{ item.type }}</i>-->
<!--                                        </el-col>-->
<!--                                        <el-col>-->
<!--                                            <i class="el-icon-zoom-in"> Area: {{ item.area }}</i>-->
<!--                                        </el-col>-->
<!--                                        </el-row>-->

<!--                                    </div>-->
<!--                                </el-col>-->
<!--                            </el-row>-->
                        </el-card>
                        </template>
                        <template v-else>
                            <div class="empty-label" >
<!--                            <label>You haven't post any property.</label>-->
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

            this.$axios
                .get('/property/information')
                .then(response => {
                    this.propList = response.data.result;
                })
                .catch(function (error) {
                    console.log(error)
                })

            if(this.propList.length){
                this.isEmpty=false;
            }
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

    &:hover {
        cursor: pointer;
        transform: scale(1.02);
    }

    h3{
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
/*.image {*/
/*    width: 200px;*/
/*    height: 200px;*/
/*    display: block;*/
/*}*/

</style>
