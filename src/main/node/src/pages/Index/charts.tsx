import * as Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';
import { Card, Col, Row, Tabs } from 'antd';

import React, {Component} from 'react';

const options: Highcharts.Options = {
    chart: {
        type: 'bar',
        height: 800,
    },
    credits: {
        enabled: false
    },
    // title: {
    //     text: 'Visa Status Summary',
    //     useHTML: true,
    //     style: {
    //         fontSize: '3vh',
    //         textAlign: 'center'
    //     }
    // },
    // subtitle: {
    //     text: 'Source: ' +
    //         '<a href="https://www.ssb.no/en/statbank/table/08940/" ' +
    //         'target="_blank">SSB</a>'
    // },
    colors: [
        'green',
        'orange',
        'red'
    ],
    xAxis: {
        categories: [
            '2010',
            '2011',
            '2012',
            '2013',
            '2014',
            '2015',
            '2016',
            '2017',
            '2018',
            '2019',
            '2020',
            '2021',
            '2022'
        ],
        // crosshair: true,
        // title: {
        //     useHTML: true,
        //     text: "time",
        //     style: {
        //         fontSize: '20px',
        //     }
        // }
    },
    yAxis: {
        // title: {
        //     useHTML: true,
        //     text: 'Number of Cases',
        //     style: {
        //         fontSize: '20px'
        //     }
        // }
        softMin: 0,
        softMax: 10,
        tickInterval: 5,
    },
    tooltip: {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y:.0f}</b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true
    },
    plotOptions: {
        series: {
            stacking: 'normal',
            dataLabels: {
                enabled: false
            },
        },
    },
    series: [{
        type: 'bar',
        name: 'Issued',
        data: [1,2,3,4,5,6,7,8,9,10,11,12,13],
    }, {
        type: 'bar',
        name: 'Pending',
        data: [13,12,11,10,9,8,7,6,5,4,3,2,1]

    }, {
        type: 'bar',
        name: 'Rejected',
        data: [1,2,3,4,5,6,7,8,9,10,11,12,13]

    }]
}

const Charts = (props: HighchartsReact.Props) => {

    options.series = props.options?.series;
    options.xAxis = props.options?.xAxis;
    options.title = props.options?.title;

    return (
        <div>
            <Row>
                <Col span={24}>
                    <HighchartsReact
                        highcharts={Highcharts}
                        options={options}
                    />
                </Col>
            </Row>
        </div>
    );

}

export default Charts;