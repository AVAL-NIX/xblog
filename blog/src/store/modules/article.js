// initial state
const state = {
  top:true,
  articleTop:false,
  title:''
}
// getters
const getters = {
  getToc: state => state.toc,
  getArticleToc: state => state.articleTop
}
// actions
const actions = {

}
// mutations
const mutations = {
    changeTop (state) {
        state.top = !state.top
        state.articleTop = !state.articleTop
    },
    setTitle(state, title){
        state.title = title
    }
}
export default {
  state,
  getters,
  actions,
  mutations
}