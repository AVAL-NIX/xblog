 <template>
 <div class="Backtop">
        <el-row :gutter="8">
            <el-col :span="12  " :offset="6">
                <div>
                    <el-link>360</el-link>
                    <el-link type="primary">baidu</el-link>
                    <el-link type="success">alibaba</el-link>
                    <el-link type="warning">tengxun</el-link>
                    <el-link type="danger">google</el-link>
                    <el-link type="info">youdao</el-link>
                </div>

            </el-col>
        </el-row>

        <el-backtop target="" :bottom="30" :right="30">
            <div
            style="{
                height: 100%;
                width: 100%;
                background-color: #f2f5f6;
                box-shadow: 0 0 6px rgba(0,0,0, .12);
                text-align: center;
                line-height: 40px;
                color: #1989fa;
            }"
            >
            UP
            </div>
        </el-backtop>
    </div>
</template>

<script>
    export default {
        name: 'Backtop'
    }
</script>

<style scoped>
    .Backtop{
        text-align: center;
    }
</style>