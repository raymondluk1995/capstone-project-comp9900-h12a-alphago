<template>
    <div class="header">
        <el-row type="flex" justify="space-around" style="align-items: center;">
            <el-col :span="4">
<!--                <el-avatar :size="50" :src="logo"></el-avatar>-->
                <img id="logo" @click="goto('home')" src="@/assets/Alogo.png"
                             alt="render failure" style="width: 25%; height:25%; cursor: pointer;">
            </el-col>
            <el-col :span="6">
                <el-row type="flex" justify="center">
<!--                    <h1 style="font-family:giveny;color: #222222;margin-left: 20px">ALPHAGO AUCTION</h1>-->
<!--                    <h3 style="font-family:Georgia;">-->
<!--                        AlphaGo Auction-->
<!--                    </h3>-->
                </el-row>
            </el-col>
            <el-col :span="2">
            <el-row type="flex" justify="right" style="float:right">
                    <slot></slot>
            </el-row>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import $ from 'jquery'
export default {
  data() {
    return {
    };
  },
    mounted(){
        $("#logo").hover(function(event) {
            $(this).stop().animate({"opacity": "0.5"}, 300);
        });
        $("#logo").mouseleave(function(event) {
            $(this).stop().animate({"opacity": "1"}, 300);
        });
    },
  methods: {
    goto(name) {
      // console.log(name);
      if(this.$route.path!='/alpha'){
        this.$router.push({ name: name });
      }
    },
  },
};
</script>

<style scoped lang="scss">
.header {
  padding: 15px;
  border-bottom: 2px solid #f2f2f2;
    /*box-shadow: 0 2px 10px 4px #d5dbea;*/
    /*height:120px;*/
    z-index: 10;
}


</style>
