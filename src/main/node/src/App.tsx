import React, { useState } from 'react';
import { Routes, Route, useNavigate, useLocation } from "react-router-dom";
import { Layout, Menu, Image, Button} from 'antd';
import './App.css';

import xiaohongshu from './xiaohongshu.jpg';

import STATIC_ROUTES from './routes';

import 'antd/dist/antd.css';
import {ButtonShape} from "antd/lib/button/button";

const { Header, Content, Footer, Sider } = Layout;

function App() {

  const navigate = useNavigate();
  const location = useLocation();
  const [visible, setVisible] = useState(false);

  return (
    <div className="App">
      <Layout style={{ minHeight: '80vh' }}>
        {/* 左侧导航栏 */}
        <Header>
            {/*<div className="logo" style={{float:"left", marginRight: "1vw"}}>*/}
            {/*    <Avatar src={logo}/>*/}
            {/*</div>*/}
            <Menu
                theme="dark" mode="horizontal"
                items={STATIC_ROUTES.map(item => ({
                    key: item.path,
                    label: item.label
                    }))}
                selectedKeys={[location.pathname ? location.pathname : '/']}
                onClick={(value) => {
                  //setCurPath(value.key)
                  navigate(value.key)}
                }
                style={{fontSize: "14px"}}
            />
        </Header>

        <Layout className="site-layout">
          {/*<Header className="site-layout-background" style={{ padding: 0 }} />*/}
          <Content>
            <Routes>
              <>
                {
                  STATIC_ROUTES.map((item, index) => <Route key={"route"+index} path={item.path} element={item.component} />)
                }
              </>
            </Routes>
          </Content>
            <Footer>加入
                <Button
                    size={"small"}
                    type="link"
                    onClick={()=>{setVisible(true)}}
                >
                    小红书
                </Button>
                群聊与其他人分享你的CASE!
                <Image
                    width={200}
                    src={xiaohongshu}
                    style={{ display: 'none' }}
                    preview={{
                        visible,
                        onVisibleChange: (value) => {
                            setVisible(value);
                        },
                    }}
                />
                <br/>
                Join our <a href="https://t.me/usvisastatus">Telegram Group </a>to discuss more about US visa!
            </Footer>
        </Layout>
       </Layout>
     </div>
  );
}

export default App;
