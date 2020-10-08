<template>
    <div class="user_profile">
        <Header>
        <el-button round type="primary" @click="back">Back</el-button> 
        </Header>
        
    </div>
</template>
<script>
import Header from "@/components/Header.vue";
export default {
  name: 'Home',
  components: {
    Header
  },data() {
    return {
      flag: flase // 默认不显示返回按钮
    };
  },
  methods: {
      back() {
        this.$router.go(-1);
      },
  }

}
</script>