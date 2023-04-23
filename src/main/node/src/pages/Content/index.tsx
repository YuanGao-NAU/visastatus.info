import {Card, Carousel, Col, Drawer, Space, Row} from "antd"
import React, { useState } from "react";

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
    Typography,
    message, Alert
} from 'antd';

const { Title, Text } = Typography;

const Content = () => {
    return (
    <Row>
        <Col>
            <Title style={{fontSize:"2vh"}}>We help you to track your case!</Title>
            <Text>- The data is from Ceac website</Text>
            <Text>- Provide your visa application ID and where you applied for your visa</Text>
            <Text type="warning">- Leave your email and  </Text>
        </Col>
    </Row>
    )
}

export default Content