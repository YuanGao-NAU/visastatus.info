/**
 * 路由列表
 */
import Index from "./pages/Index";
import Detail from "./pages/Detail";
import Form from "./pages/Form"
import Cases from "./pages/Cases";

// const STATIC_ROUTES = [{
//     path: '/',
//     label: '首页',
//     component: <Index key={"1"}/>,
// },{
//     path: '/table',
//     label: '表格',
//     component: <Table />,
// },{
//     path: '/detail',
//     label: '详情',
//     component: <Detail />
// },{
//     path: '/form',
//     label: '表单',
//     component: <Form />
// },{
//     path: '/content',
//     label: '内容',
//     component: <Content />
// }]

const STATIC_ROUTES = [{
    path: '/',
    label: 'Home',
    component: <Index key={"1"}/>,
},{
    path: '/form',
    label: 'Track Your Case',
    component: <Form/>
}, {
    path: '/detail',
    label: 'Case Detail',
    component: <Detail/>
}, {
    path: '/cases',
    label: 'Cases',
    component: <Cases/>
}]
// ,{
//     path: '/about',
//     label: 'About',
//     component: <Content />
// }]

export default STATIC_ROUTES