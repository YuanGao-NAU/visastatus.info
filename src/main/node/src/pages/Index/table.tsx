import {Card, Col, Row, Tabs, Table, Typography, Timeline, Popover, Button} from 'antd';
import type { ColumnsType } from 'antd/es/table';
import React from 'react';
import {caseHistory} from "../Detail";
import {CheckCircleTwoTone, ClockCircleOutlined, CloseCircleTwoTone, LoadingOutlined} from "@ant-design/icons";
const { Title } = Typography;

interface CaseType {
    id: string;
    location: string;
    category: string;
    isSTEM: string;
    isUniversitySensitive: string;
    interviewDate: string;
    updatedDate: string;
    status: string;
}

export interface TableDataType {
    //cid: string;
    aCase: CaseType;
    caseHistory: caseHistory[];
}

export type Props = {cases: TableDataType[]};

const VisaTable = (data: Props) => {

    function showCaseHistory(params: caseHistory[]) {

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

    const columns: ColumnsType<TableDataType> = [
        {
            title: 'ID',
            width: 50,
            dataIndex: ['aCase','id'],
            key: 'id',
            fixed: 'left',
        },
        {
            title: 'Location',
            width: 100,
            dataIndex: ['aCase','location'],
            key: 'location',
            fixed: 'left',
        },
        { title: 'Category', dataIndex: ['aCase','category'], key: 'category', width: 100 },
        { title: 'Is STEM?', dataIndex: ['aCase','isSTEM'], key: 'isSTEM', width: 100 },
        { title: 'Is University Sensitive?', dataIndex: ['aCase','isUniversitySensitive'], key: 'isUniversitySensitive' },
        { title: 'Interview Date', dataIndex: ['aCase','interviewDate'], key: 'interviewDate' },
        { title: 'Update Date', dataIndex: ['aCase','updatedDate'], key: 'updatedDate' },
        {
            title: 'Latest status',
            dataIndex: 'status',
            key: 'status',
            render: (_:any, record: TableDataType) => {
                const caseHistory = record.caseHistory;
                return (
                    <text>
                        {caseHistory[caseHistory.length-1].actualStatus}
                    </text>
                )
            },
        },
        {
            title: 'Details',
            dataIndex: 'caseHistory',
            key: 'caseHistory',
            render: (_: any, record: TableDataType) => {
                return (
                    <>
                        <Popover trigger="click" content={
                            showCaseHistory(record.caseHistory)}
                        >
                            <Button>Show Details</Button>
                        </Popover>
                    </>
                )
            },
        },
    ];

    return (
        <Table columns={columns}
               dataSource={data.cases}
               scroll={{ x: 1300 }}
               rowClassName={(record, index, intent) => (record.aCase.status === "Issued" || record.aCase.status === "Approved" ? "highlightedRowInTable" : "") }
               title={()=>(<Title style={{fontSize:"2vh", textAlign:"center"}}>Cases</Title>)}/>
    );
};

export default VisaTable;