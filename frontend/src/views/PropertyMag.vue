<template>
    <div class="propmag">
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

        <el-row class="win" type="flex" justify="center">
            <el-col :span="8">
                <el-row style="background-color: #e4e9f3">
                    <el-select
                            v-model="filter"
                            style="float:right;margin:10px 30px"
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
                </el-row>

                <el-row class="property-list">
                <el-card
                        class="card"
                        :class="addStatusColor(item.status)"
                        v-for="item in propList"
                        :key="item.id"
                >
                    <el-row>
                        <div @click="selectItem(item)">
                            <h4>{{ item.address }}</h4>
                            <p>{{ item.status_detail }}</p>
                        </div>
                        <el-row type="flex" justify="end">
                            <el-button type="" round @click="removeItem(item.id)">Remove</el-button>
                        </el-row>
                    </el-row>
                </el-card>
                </el-row>


            </el-col>
            <el-col :span="18" class="property-item">
                <section>
                    <el-row type="flex" justify="space-between" style="margin-bottom: 20px">
                        <h2>{{ propInfo.title }}</h2>
                        <el-button type="primary" icon="el-icon-right" round @click="goto('propreg')">Register New Auction</el-button>
                    </el-row>
                    <el-carousel :interval="5000" arrow="always" :width="cwidth" :height="cheight" style="margin: 0 50% 0 0">
                        <el-carousel-item v-for="pic in propInfo.photos" :key="propInfo.pid">
                            <img :src="pic"  width="100%" height="100%" alt=""/>
                        </el-carousel-item>
                    </el-carousel>
<!--                    <el-form >-->
<!--                        <el-form-item>-->
<!--                        <el-upload-->
<!--                            multiple-->
<!--                            class="avatar-uploader"-->
<!--                            action="upload"-->
<!--                            accept="image/*"-->
<!--                            :auto-upload="false"-->
<!--                            list-type="picture-card"-->
<!--                            :limit="10"-->
<!--                            :file-list="this.propInfo.photos"-->
<!--                            :on-remove="handleRemove"-->
<!--                            :on-exceed="exceedTips"-->
<!--                            :on-change="imgBroadcastChange"-->
<!--                            :before-upload="beforeAvatarUpload"-->
<!--                    >-->
<!--                        <i class="el-icon-plus"></i>-->
<!--                    </el-upload>-->
<!--                        </el-form-item>-->
<!--                    </el-form>-->

                </section>

                <el-row type="flex" justify="end">
<!--                    <el-button>Edit</el-button>-->
                </el-row>

                <section>
                    <el-row class="mh20" type="flex">
                        <el-col :span="7">
                            <i class="el-icon-toilet-paper"> Bathroom Number: <span> {{ propInfo.bathroomNum}} </span></i>
                        </el-col>
                        <el-col :span="7">
                            <i class="el-icon-house"> Bedroom Number: <span> {{ propInfo.bedroomNum}} </span></i>
                        </el-col>
                        <el-col :span="7">
                            <i class="el-icon-truck"> Garage Number: <span> {{ propInfo.garageNum}} </span></i>
                        </el-col>
                    </el-row>
                    <el-row class="mh20" type="flex">
                        <el-col :span="7">
                            <i class="el-icon-info"> Type: {{ propInfo.type }}</i>
                        </el-col>
                        <el-col :span="7">
                            <i class="el-icon-full-screen"> Area: {{ propInfo.area }}</i>
                        </el-col>
                    </el-row>

                    <el-row type="flex" style="align-items:center">
                        <span class="mr20">Keywords </span>
<!--                        <el-button>Edit</el-button>-->
                    </el-row>

                    <el-row type="flex" style="margin-bottom: 10px;">
                        <el-tag v-for="tag in (propInfo.position||'').split(',')" effect="plain" :key="tag">{{ tag }}</el-tag>
                    </el-row>

                    <el-row type="flex" style="margin-bottom: 10px;">
                        <el-tag v-for="tag in (propInfo.detail||'').split(',')" :key="tag">{{ tag }}</el-tag>
                    </el-row>

                </section>

                <el-row class="mh20" type="flex" style="align-items:center">
                    <span class="mr20">Description </span>
<!--                    <el-button v-if="!canEditDesc" @click="editDesc">Edit</el-button>-->
<!--                    <el-button v-else @click="saveDesc">Save</el-button>-->
                </el-row>

                <el-card>
                    <p>{{ propInfo.description }}</p>
<!--                    <el-input v-else type="textarea" v-model="desc"></el-input>-->
                </el-card>

                <section class="mh20" v-if="propInfo.auction">
                    <h5>Auction</h5>
                    <p>Start Date: {{ showdate(propInfo.startDate) }}</p>
                    <p>End Date: {{ showdate(propInfo.endDate) }}</p>
                    <p>Reserved Price: ${{ propInfo.price }}</p>
                </section>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
    import { mapActions } from "vuex";
    import dayjs from "dayjs";

    export default {
        name: "Auction",
        components: {
            Header,
        },
        props: {
            cheight: {
                type: String,
                default: '300px'
            },
            cwidth:{
                type: String,
                default: '200px'
            }
        },

        data() {
            return {
                hasLogin:false,
                filter: "all",
                photos:[],
                options: [
                    {
                        value: "all",
                        label: "all",
                    },
                    {
                        value: "NR",
                        label: "Not register",
                    },
                    {
                        value: "O",
                        label: "Over",
                    },
                    {
                        value: "P",
                        label: "In process",
                    },
                ],
                urlObjImg:{},
                originPropertyList: [
                ],
                propList: [],
                propInfo: {},
                rules: {
                },
            };
        },
        created(){
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
                .get('/properties')
                .then(response => {
                    this.originPropertyList = response.data.result
                })
                .catch(function (error) {
                    console.log(error)
                })

        },
        mounted() {
            this.propList = this.originPropertyList;
            this.propInfo = this.originPropertyList.length !== 0 && this.originPropertyList[0];
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
                        this.$router.push("/notification");
                        break;
                    case "logout":
                        this.logout();
                        break;
                    default:
                        break;
                }
            },
            addStatusColor(status) {
                const colors = new Map([
                    ["NR", "status-not-register"],
                    ["P", "status-process"],
                    ["O", "status-over"],
                ]);
                return colors.get(status);
            },
            showdate(t){
                return dayjs(t).format("YYYY-MM-DD HH:mm:ss")
            },

            changeSearch(value) {
                console.log(value);
                let filterPropertyList = this.originPropertyList.filter((e) => {
                    return e.status === value;
                });
                this.propList = filterPropertyList;
            },

            selectItem(item) {
                this.propInfo = item;
            },
            removeItem() {
                    this.$confirm('Remove this property?', 'Alert', {
                        confirmButtonText: 'Confirm',
                        cancelButtonText: 'Cancel',
                        type: 'warning'
                    }).then(() => {
                        this.$axios.delete('/properties/delete' + this.propInfo.id)
                            .then((response) => {
                                if (response.status >= 200 && response.status < 300){
                                    if (response.data.code === 200){
                                        this.$message({
                                            type: 'success',
                                            message: 'Remove!'
                                        });
                                        location.reload()
                                    }else{
                                        console.log(response.msg)
                                    }
                                }else{
                                    console.log(response.msg)
                                }
                            })
                    })

                // this.propList = this.propList.filter((e) => {
                //     return e.id !== id;
                // });
            },
            editphoto(){
                this.propInfo.photos.forEach(val => { // 通过遍历得到数据库中的照片并进行照片回显
                    this.photos=[];
                    this.urlObjImg.url = val;
                    this.photos.push(this.urlObjImg) // 把数据库中的照片添加到fileListImg里面
                })
            },


            beforeAvatarUpload(file) {
                const isLt2M = file.size / 1024 / 1024 < 2;
                let types = ["image/jpeg", "image/jpg", "image/png"];
                const isImage = types.includes(file.type);
                if (!isImage) {
                    this.$message.error("上传图片只能是 JPG、JPEG、PNG 格式!");
                }
                if (!isLt2M) {
                    this.$message.error("Image size can not larger than 2MB!");
                }
                return isImage && isLt2M;
            },
            handleRemove(file, fileList) {
                const IMG = file.raw;
                const INDEX = this.form4.imageRaw.indexOf(IMG);
                this.form4.imageRaw.splice(INDEX, 1);
                this.form4.imageUrl.splice(INDEX, 1);
                this.form4.hasupload = this.form4.hasupload - 1;
                console.log(file, fileList);
            },
            imgBroadcastChange(file) {
                this.form4.hasupload = this.form4.hasupload + 1;
                this.form4.imageRaw.push(file.raw);
                this.form4.imageUrl.push(URL.createObjectURL(file.raw));
            },
            exceedTips: function () {
                this.$message.error("Maximum 10 photos.");
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
    .mh20 {
        margin: 20px 0;
    }
    .mr20 {
        margin-right: 20px;
    }
    .user {
        display: flex;
        align-items: center;
        p {
            margin-left: 10px;
        }
    }
    .win {
        height: calc(100vh - 200px);
        /*margin: 0 20px;*/

    }
    .property-list{
        /*margin-top: 40px;*/
        height: calc(100vh - 260px);
        padding: 10px;
        overflow-y: scroll;
        background-color: #f1f1f1;
        box-shadow: inset 2px 1px 5px 4px #d5dbea;
    }
    .property-item {
        margin-top: 40px;
        padding: 10px;
        overflow-y: scroll;
        margin-right:50px;
        margin-left:50px;
        /*box-shadow: 2px 1px 5px 4px #d5dbea;*/
    }
    .card {
        margin: 20px 20px;
        &:hover {
            cursor: pointer;
            background-color: #d6e2e7;
            color: #f00;
        }
        h3,
        p {
            line-height: 2.5;
        }
    }
    .status-not-register {
        border-left: 15px solid #e7b993;
    }
    .status-process {
        border-left: 15px solid #aed991;
    }
    .status-over {
        border-left: 15px solid #8a97a6;
    }
    .swiper-slide {
        height: 500px;
    }
</style>
