"use strict";

AUI.add("cluster-monitor",
        function (A) {
            A.ClusterMonitor = {
                chart : function (options) {

                    var axes = {
                        Time : {
                            labelFormat : "%T"
                        }
                    };

                    var marker = {
                        marker : {
                            height : 4,
                            width  : 5
                        },
                        line   : {
                            weight : 3
                        }
                    };

                    var failureFn = function (e) {
                        alert("Oops... something went wrong while rendering the chart: " + e);
                    };

                    var successFn = function (e) {

                        var raw = e.data && (e.data.responseText || e.data);
                        var json = A.JSON.parse(raw);
                        var data = options.mapper(json);

                        var styles = {};
                        A.each(data, function (item) {
                            for (var property in item) {
                                if (property !== "Time") {
                                    if (!styles.series) {
                                        styles.series = {};
                                    }
                                    if (!styles.series[property]) {
                                        styles.series[property] = marker;
                                    }
                                }
                            }
                        });

                        var chart = new A.Chart({
                                                    axes                : axes,
                                                    categoryKey         : "Time",
                                                    categoryType        : "time",
                                                    dataProvider        : data,
                                                    horizontalGridlines : true,
                                                    interactionType     : "planar",
                                                    styles              : styles,
                                                    verticalGridlines   : true
                                                });
                        chart.render(options.target);
                    };

                    var datasource = new A.DataSource.IO({
                                                             source : options.source
                                                         });
                    datasource.sendRequest({
                                               on : {
                                                   failure : failureFn,
                                                   success : successFn
                                               }
                                           });
                }
            }
        },
        "0.1",
        {
            requires : ["charts",
                        "datasource-io",
                        "json-parse"]
        });
