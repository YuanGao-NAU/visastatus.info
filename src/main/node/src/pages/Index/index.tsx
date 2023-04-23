import { Card, Col, Row, Tabs, Typography } from 'antd';
import React, {useEffect} from 'react';
import Charts from "./charts";
import VisaTable, {TableDataType, Props} from "./table";
import axios from "axios";
import * as Highcharts from "highcharts";
import {
    HomeOutlined,
    LoadingOutlined,
    SettingFilled,
    SmileOutlined,
    SyncOutlined,
} from '@ant-design/icons';

const { Title, Text } = Typography;

interface CaseSummary {
    type: string,
    name: string,       //Issued, Pending, Rejected
    data: number[],
    total: number
}

interface Cases {
    pendingData: CaseSummary;
    rejectedData: CaseSummary;
    issuedData: CaseSummary;
    dayList: string[];
}

interface IndexProps {
    key: string;
    data: Cases;
}

const fakeData:IndexProps = {
    key: "3",
    data: {
        pendingData: {
            type: "bar",
            name: "Pending",
            data: [0],
            total: 0
        },
        rejectedData: {
            type: "bar",
            name: "Refused",
            data: [0],
            total: 0
        },
        issuedData: {
            type: "bar",
            name: "Issued",
            data: [0],
            total: 0
        },
        dayList: ["2022-04-01"],
    }
}

const baseURL = "/stat";

const Index = (props : IndexProps) => {

    function updateData(key: string) {
        let result!:Cases;
        setIsLoading(true);
        if(key === "3") {
            axios.post(baseURL + "/getStatisticsWithinOneYear")
                .then((response)=>{
                    console.log(response.data);
                    setKeyAndData({
                        key: key,
                        data:response.data});
                    setIsLoading(false);
                }).catch(error=>{
                    console.log(error);
                })
        } else if(key === "2") {
            axios.post(baseURL + "/getStatisticsOfThisMonth")
                .then((response)=>{
                    console.log(response.data);
                    setKeyAndData({
                        key: key,
                        data:response.data});
                    setIsLoading(false);
                }).catch(error=>{
                    console.log(error);
                })
        } else {
            axios.post(baseURL + "/getStatisticsByDateInterval")
                .then((response)=>{
                    console.log(response.data);
                    setKeyAndData({
                        key: key,
                        data:response.data});
                    setIsLoading(false);
                }).catch(error=>{
                    console.log(error);
                })
        }
    }

    const [keyAndData, setKeyAndData] = React.useState(fakeData);
    const [isLoading, setIsLoading] = React.useState(true);
    useEffect(()=>{
        updateData("3");
    }, []);

    function getChartTitle(key: string) {
        if(key === "1") {
            return "Visa Status Summary (Per Year)";
        } else if (key === "2") {
            return "Visa Status Summary (Per Day)";
        } else if (key === "3") {
            return "Visa Status Summary (Per Week)";
        }
        return "Visa Status Summary";
    }

    const options: Highcharts.Options = {
            series: [
                {
                    type: "bar",
                    name: keyAndData.data.issuedData.name,
                    data: keyAndData.data.issuedData.data
                },
                {
                    type: "bar",
                    name: keyAndData.data.pendingData.name,
                    data: keyAndData.data.pendingData.data
                },
                {
                    type: "bar",
                    name: keyAndData.data.rejectedData.name,
                    data: keyAndData.data.rejectedData.data
                }
            ],
            xAxis: {
                categories: keyAndData.data.dayList,
                reversed: false,
            },
            title: {
                text: getChartTitle(keyAndData.key),
                useHTML: true,
                style: {
                    fontSize: '3vh',
                    textAlign: 'center'
                }
            }
        }

    const onChange = (key: string) => {
        updateData(key);
    }

    function card(result: IndexProps) {



        return (
            <div className="stats">

                <div className="summaryCard">
                    <Row >
                        <Col span={8}>
                            <Card title={<Title style={{fontSize:"2vh"}}>Issued</Title>} bordered={false} style={{backgroundColor:"green", fontSize:'3vh'}}>
                                {result.data.issuedData.total}
                            </Card>
                        </Col>
                        <Col span={8}>
                            <Card title={<Title style={{fontSize:"2vh"}}>Pending</Title>} bordered={false} style={{backgroundColor:"orange", fontSize:'3vh'}}>
                                {result.data.pendingData.total}
                            </Card>
                        </Col>
                        <Col span={8}>
                            <Card title={<Title style={{fontSize:"2vh"}}>Refused</Title>} bordered={false} style={{backgroundColor:"red", fontSize:'3vh'}}>
                                {result.data.rejectedData.total}
                            </Card>
                        </Col>
                    </Row>
                </div>
                <br/>
                <br/>
                <Row>
                    <Col
                        xs={{ span: 22, push: 1 }}
                        sm={{ span: 22, push: 1 }}
                        md={{ span: 20, push: 2 }}
                        lg={{ span: 16, push: 4 }}
                        xl={{ span: 14, push: 5 }}
                    >
                        <Charts options={options}/>
                    </Col>
                </Row>
                <br/>
                <br/>
                {/*<VisaTable cases={result.data.cases}/>*/}
            </div>
        )
    }


    if(!isLoading) {
        return (
        <div>
            <div className="period">
                <Tabs
                    defaultActiveKey={keyAndData.key}
                    onChange={onChange}
                >
                    <Tabs.TabPane tab="All" key="1" disabled={true}>
                    </Tabs.TabPane>
                    <Tabs.TabPane tab="This Month" key="2">
                    </Tabs.TabPane>
                    <Tabs.TabPane tab="Last 12 Months" key="3">
                    </Tabs.TabPane>
                </Tabs>
                {card(keyAndData)}
            </div>
        </div>
        )
    } else {
        return (
            <Row>
                <Col span={11}></Col>
                <Col span={2}>
                    <LoadingOutlined
                        style={
                        {
                            height:"3vh",
                            alignSelf:"auto",
                            fontSize:"4vh"

                        }
                    }
                    />
                </Col>
                <Col span={11}></Col>
            </Row>
        )
    }

    const formattedDateString = (offset: number) => {
        const today = new Date();
        const yyyy = today.getFullYear() - offset;
        let mm = today.getMonth() + 1; // Months start at 0!
        let dd = today.getDate();

        let ddString = "";
        let mmString = "";

        if (dd < 10) ddString = '0' + dd;
        if (mm < 10) mmString = '0' + mm;

        const formattedToday = yyyy + "-" + mmString + "-" + ddString;
        return formattedToday;
    }
}

export default Index;