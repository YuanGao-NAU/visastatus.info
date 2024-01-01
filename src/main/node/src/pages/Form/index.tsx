import React, { useState } from 'react';
import { PlusOutlined } from '@ant-design/icons';
import {
    Form,
    Input,
    Button,
    Radio,
    Select,
    Cascader,
    DatePicker,
    InputNumber,
    TreeSelect,
    Switch,
    Checkbox,
    Upload,
    Row,
    Col,
    Typography,
    message, Alert
} from 'antd';
import LOCATIONS, {OPTIONS, STATUS, VISA_TYPES} from "./utils";
import type { Moment } from 'moment';
import moment from "moment";
import type { CheckboxChangeEvent } from 'antd/es/checkbox';
import axios from "axios";

const { Title, Text } = Typography;
const { RangePicker } = DatePicker;
const { TextArea } = Input;
const {Option} = Select;

const baseURL = "/case";

const FormPage = () => {
    const [componentDisabled, setComponentDisabled] = useState<boolean>(false);
    const [showEmailInfo, setShowEmailInfo] = useState<boolean>(false);
    // const onFormLayoutChange = ({ disabled }: { disabled: boolean }) => {
    //   setComponentDisabled(disabled);
    // };

    const onFinish = (values: any) => {
        values.interviewDate = moment(values.interviewDate).format("YYYY-MM-DD");
        console.log('Success:', values);
        submitApplication(values);
    };

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
    };

    const disabledDate = (current: Moment) => {
        //console.log(current)
        return current > moment().add(0,'day');
    }

    const showMessage = (data: any) => {
        setShowEmailInfo(true);
        switch(data.status) {
            case "noData":
                message.warning(data.message);
                break;
            case "invalidCaseNumber":
                message.error("APP ID is invalid");
                break;
            case "duplicatedCase":
                message.warning("Please do not submit a case multiple times!");
                break;
            case "serverDown":
                message.info(data.message);
                break;
            default:
                message.info("Application submitted successfully!");
        }
    }

    const showEmailInfoFunc = () => {
        if(showEmailInfo) {
            return (
                <Row>
                    <Col span={12}>
                        <Alert
                            message={<text>Please add email address <a href={"mailto:admin@visastatus.info"}>admin@visastatus.info</a> to you contact list to make sure you can receive updates in time!</text>}
                            type="info"
                            showIcon
                            closable
                        />
                    </Col>
                </Row>
            )
        } else {
            return null;
        }
    }

    const submitApplication = (values: any) => {
        setComponentDisabled(true);
        axios.post(baseURL + "/add", values)
            .then((response)=>{
                showMessage(response.data);
                setComponentDisabled(false);
            }).catch(error=>{
                console.log(error);
                setComponentDisabled(false);
                message.error("Something was wrong, please try again later!");
            })
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
                    <Title>
                        How it works?
                    </Title>
                    <Text>By submitting your case in this page with visa application ID, we can help you to track your case automatically!</Text>
                    <br/>
                    <Text>Once you have submitted your case, go to "Case Detail" to see the details of your case!</Text>
                    <br/>
                    <Text mark>An email will be sent after your case has been submitted if your email address is provided</Text>
                </Col>
            </Row>

            <br/>

            <Row>
                <Col
                    xs={{ span: 22, push: 1 }}
                    sm={{ span: 22, push: 1 }}
                    md={{ span: 20, push: 2 }}
                    lg={{ span: 16, push: 4 }}
                    xl={{ span: 14, push: 5 }}
                >
                    <Title style={{fontSize:"2vh"}}>Submit Your Case</Title>
                    <Form
                        name="form"
                        labelCol={{ span: 12 }}
                        wrapperCol={{ span: 12 }}
                        layout="vertical"
                        initialValues={{ done: false }}
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
                            // {len:10, pattern: new RegExp('/^AA*$/', 'g'), message: "The ID length should 10 and start with 'AA', which is used at https://"}
                        ]}
                            extra={<p>Same application id used on <a href="https://ceac.state.gov/CEACStatTracker/Status.aspx">Ceac website</a></p>}
                        >
                            <Input
                                placeholder={"AA123ABCDE"}
                            />
                        </Form.Item>
                        <Form.Item name="location" label="Location" rules={[
                            {
                                type: "string",
                                message: "Please select one location from the drop down menu."
                            },
                            {
                                required: true,
                                message: "Please select one location"
                            }
                        ]}>
                            <Select defaultValue="- SELECT ONE -" showSearch >
                                {LOCATIONS.map((location, index)=>(
                                    <Option key={"location"+index} value={location}>{location}</Option>
                                ))}
                            </Select>
                        </Form.Item>
                        <Form.Item name="category" label="Visa Category" required={true} rules={[
                            {
                                type: "string",
                                message: "Please select one category from the drop down menu."
                            },
                            {
                                required: true,
                                message: "Please select one category"
                            }
                        ]}>
                            <Select defaultValue="- SELECT ONE -" showSearch>
                                {VISA_TYPES.map((visaType, index)=>(
                                    <Option key={"visaType"+index} value={visaType}>{visaType}</Option>
                                ))}
                            </Select>
                        </Form.Item>

                        <Form.Item name="status" label="Current Status" required={true} rules={[
                            {
                                required: true,
                                message: "Please select current status"
                            }
                        ]}>
                            <Select defaultValue="- SELECT ONE -">
                                {STATUS.map((status, index)=>(
                                    <Option key={"status"+index} value={status}>{status}</Option>
                                ))}
                            </Select>
                        </Form.Item>

                        <Form.Item name="interviewDate" label="Interview Date" rules={[
                            {
                                required: true,
                                message: "Please select the date you were interviewed"
                            }
                        ]}>
                            <DatePicker format="YYYY-MM-DD" disabledDate={disabledDate}/>
                        </Form.Item>

                        <Form.Item name="done" valuePropName="checked">
                            <Checkbox>
                                <Text mark>Is this the final decision of your case?</Text>
                            </Checkbox>
                        </Form.Item>

                        <Form.Item
                            initialValue="/"
                            name="isSTEM"
                            label={<Text>Is your major one of the STEM majors?</Text>}
                        >
                            <Select>
                                {OPTIONS.map((option, index)=>(
                                    <Option key={"option"+index} value={option}>{option}</Option>
                                ))}
                            </Select>
                        </Form.Item>
                        <Form.Item
                            initialValue="/"
                            name="isUniversitySensitive"
                            label={<p>Are you from a university or entity influenced by <a href={"https://en.wikipedia.org/wiki/Proclamation_10043"}>PP 10043</a>? <a href={"https://www.10043.org/support"}>More</a></p>}
                        >
                            <Select>
                                {OPTIONS.map((option, index)=>(
                                    <Option key={"option"+index} value={option}>{option}</Option>
                                ))}
                            </Select>
                        </Form.Item>

                        <Form.Item
                            name="email" label="Email address (Optional)"
                            rules={[
                                {
                                    type: "email",
                                    message: "Format is not correct"
                                }
                            ]}
                            extra={<p style={{color:"orange"}}>Leave you email address so that you can be notified when there's a update on your case</p>}
                        >
                            <Input/>
                        </Form.Item>

                        <Form.Item
                            name="passportID"
                            label="Passport Number"
                            rules={[
                                {
                                    type: "string",
                                    message: "Please input your passport ID"
                                },
                                {
                                    required: true,
                                    message: "Please input your passport ID"
                                },
                            ]}
                        >
                            <Input/>
                        </Form.Item>

                        <Form.Item
                            name="surname"
                            label="First 5 letters of your surname"
                            rules={[
                                {
                                    type: "string",
                                    message: "Please input your surname"
                                },
                                {
                                    required: true,
                                    message: "Please input your surname"
                                },
                            ]}
                        >
                            <Input
                                placeholder={"If less than 5 letters, type all of them. No SPACE in the end"}
                            />
                        </Form.Item>

                        <Form.Item>
                            <Button type={"primary"} htmlType={"submit"}>Submit</Button>
                        </Form.Item>

                        {showEmailInfoFunc()}
                    </Form>
                </Col>
            </Row>

        </>
    );
}

export default FormPage