import React, {useEffect, useState} from "react";
import {DatePicker, message, Radio, Select, Timeline, Space} from "antd";
import {Col, Row} from "antd";
import moment, {Moment} from "moment";
import VisaTable, {Props, TableDataType} from "../Index/table";
import axios from "axios";
import {CheckCircleTwoTone, ClockCircleOutlined, CloseCircleTwoTone, LoadingOutlined} from "@ant-design/icons";
import {caseHistory} from "../Detail";
import {VISA_TYPES} from "../Form/utils";
const {Option} = Select;

const baseURL = "/cases";

const App: React.FC = () => {

    const [dateValue, setDateValue] = useState<Moment>(moment().startOf("month"));
    const [tableData, setTableData] = useState<Props>();
    const [category, setCategory] = useState<string>("");

    const disabledDate = (current: Moment) => {
        //console.log(current)
        return current <= moment("2022-12-31");
    }

    const formData = new FormData();

    const getTableData = () => {

        const config= {
            headers: {'Content-Type': 'multipart/form-data'}
        }
        const startDate = dateValue ? dateValue.startOf("month").format("YYYY-MM-DD"): moment().startOf("month").format("YYYY-MM-DD");
        const endDate = dateValue ? dateValue.endOf("month").format("YYYY-MM-DD"): moment().endOf("month").format("YYYY-MM-DD");
        formData.append("startDate", startDate);
        formData.append("endDate", endDate);
        formData.append("category", category);
        axios.post(baseURL + "/list", formData, config)
            .then((response)=>{
                setTableData(response.data);
                console.log(tableData === null);
            }).catch(error=>{
            console.log(error);
            message.error("Something was wrong, please try again later!");
        })
    }

    useEffect(()=>{getTableData()}, [dateValue, category]);

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
                    <Space>
                        <DatePicker
                            placement={"bottomLeft"}
                            picker={"month"}
                            disabledDate={disabledDate}
                            defaultValue={dateValue}
                            value={dateValue}
                            size={"large"}
                            onChange={(date:Moment|null)=>{
                                setDateValue(date ? date : moment().startOf("month"));
                            }}
                        />
                        <Select size={"large"} defaultValue="- SELECT ONE -" showSearch onChange={(value)=>{setCategory(value)}}>
                            {VISA_TYPES.map((visaType, index)=>(
                                <Option key={"visaType"+index} value={visaType}>{visaType}</Option>
                            ))}
                        </Select>
                    </Space>
                </Col>
            </Row>
            <Row>
                {tableData !== undefined && tableData !== null ?
                    <Col
                        xs={{ span: 22, push: 1 }}
                        sm={{ span: 22, push: 1 }}
                        md={{ span: 20, push: 2 }}
                        lg={{ span: 16, push: 4 }}
                        xl={{ span: 14, push: 5 }}
                    >
                    <VisaTable cases={tableData.cases}/>
                    </Col>
                    :
                    <>
                    <Col span={11}></Col>
                    <Col span={2}>
                        <LoadingOutlined
                            style={
                                {
                                    height:"3vh",
                                    alignSelf:"auto",
                                    fontSize:"4vh",
                                    alignContent:"auto"

                                }
                            }
                        />
                    </Col>
                    <Col span={11}></Col>
                    </>
                }
            </Row>
        </>
    );
};

export default App;