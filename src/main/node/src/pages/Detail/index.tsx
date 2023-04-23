import React, {useState} from 'react'
import { MoreOutlined } from '@ant-design/icons';
import {
    Button,
    Dropdown,
    Menu,
    PageHeader,
    Row,
    Space,
    Tag,
    Typography,
    Form,
    Input,
    Radio,
    Timeline,
    Alert,
    Col, message
} from 'antd';

import {
    LoadingOutlined,
    ClockCircleOutlined,
    CheckCircleTwoTone,
    CloseCircleTwoTone
} from '@ant-design/icons';

import axios from "axios";

const { Paragraph } = Typography;
const { Title, Text } = Typography;

export interface caseHistory {
    updatedDate: string;
    cid: string;
    status: string;
    message: string;
    actualStatus: string;
}

const initData: caseHistory[] = [
    {
        updatedDate: "",
        cid: "",
        status: "",
        message: "",
        actualStatus: ""
    }
];

const baseURL = "/casehistory";

const Detail = () => {
    const [detail, setDetail] = useState(false);
    const [loading, setLoading] = useState(false);
    const [componentDisabled, setComponentDisabled] = useState(false);
    const [receivedData, setReceivedData] = useState<caseHistory[]>(initData);

    const onFinish = (values: any) => {
        console.log('Success:', values);
        if(values.email === undefined) values.email = "";
        submitApplication(values);
    };

    const formData = new FormData();

    const submitApplication = (values: any) => {

        const config= {
            headers: {'Content-Type': 'multipart/form-data'}
        }
        formData.append("cid", values.cid);
        formData.append("email", values.email);
        setComponentDisabled(true);
        setDetail(false);
        setLoading(true);
        axios.post(baseURL + "/list", formData, config)
            .then((response)=>{
                setComponentDisabled(false);
                setDetail(true);
                setLoading(false);
                setReceivedData(response.data);
            }).catch(error=>{
                console.log(error);
                setComponentDisabled(false);
                setLoading(false);
                message.error("Something was wrong, please try again later!");
            })
    }

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
    };

    function showHint(params: caseHistory[]) {
        if(!detail) {
            return (
                <>
                    <br/>
                    <Alert message="A visa application id should be provide before you can see the details!" type="warning" showIcon closable />
                    <br/>
                </>
            )
        }

        if(params === undefined || params === null || params.length === 0) {
            return (
                <>
                    <br/>
                    <Alert
                        message={<text>No result found! If you haven't submit your case, please submit it <a href={"/form"}> here!</a> You may also check the id you provided</text>}
                        type="warning"
                        showIcon
                        closable
                    />
                    <br/>
                </>

            )
        }
        return null

    }

    function showCaseHistory(params: caseHistory[]) {
        if(!detail) {
            return null
        }
        if(loading) {
            return (
                <LoadingOutlined />
            )
        }

        if(params === undefined || params === null || params.length === 0) {
            return null
        }

        return (
            <>
                <Timeline mode="left">
                    {
                        params.map((item, index) => {

                            var color = "";
                            var dot: React.ReactNode;
                            if(item.status === "Issued")
                            {
                                color = "green";
                                dot = <CheckCircleTwoTone twoToneColor="#52c41a" />
                            }
                            else if(item.status === "Pending") {
                                color = "orange";
                                dot = <ClockCircleOutlined style={{ fontSize: "16px", color: "orange" }} />
                            }
                            else {
                                color = "red";
                                dot = <CloseCircleTwoTone twoToneColor="#ff0000"/>
                            }

                            return (
                                <Timeline.Item
                                    key={item.updatedDate + item.status}
                                    color={color}
                                    dot={dot}
                                >
                                    <p>
                                        {item.updatedDate} {item.actualStatus}
                                    </p>
                                    <p>
                                        {item.message}
                                    </p>
                                </Timeline.Item>
                            )}
                        )
                    }
                </Timeline>
            </>
        );
    }

    return (
        <>
            <Row>
                <Col
                    xs={{ span: 22, push: 1 }}
                    sm={{ span: 22, push: 1 }}
                    md={{ span: 20, push: 2 }}
                    lg={{ span: 16, push: 4 }}
                    xl={{ span: 14, push: 5 }}
                >

                    <Title style={{fontSize:"2vh"}}>See details of your case</Title>

                    {showHint(receivedData)}

                    <Form
                        name="form"
                        labelCol={{ span: 12 }}
                        wrapperCol={{ span: 12 }}
                        layout="vertical"
                        initialValues={{ done: true }}
                        onFinish={onFinish}
                        onFinishFailed={onFinishFailed}
                        autoComplete="off"
                        disabled={componentDisabled}
                    >
                        <Form.Item
                            name="cid" label="Visa Application ID"
                            rules={[
                                {
                                    type: "string",
                                    message: "Please input your application id"
                                },
                                {
                                    required: true,
                                    message: "Please input your application id"
                                },
                            ]}
                            extra={<p>Same application id used on <a href="https://ceac.state.gov/CEACStatTracker/Status.aspx">Ceac website</a></p>}
                        >
                            <Input
                                placeholder={"AA123ABCDE"}
                            />
                        </Form.Item>

                        <Form.Item
                            name="email" label="Email address"
                            rules={[
                                {
                                    type: "email",
                                    message: "Format is not correct"
                                }
                            ]}
                            extra={<p>You can provide your email address here if you forgot to do so when you submit your case.</p>}
                        >
                            <Input
                            />
                        </Form.Item>

                        <Form.Item>
                            <Button type={"primary"} htmlType={"submit"}>Submit</Button>
                        </Form.Item>
                    </Form>

                    {showCaseHistory(receivedData.reverse())}

                </Col>
            </Row>
        </>
            )
}

export default Detail;