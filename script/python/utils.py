import logging
from tqdm import tqdm
import pymysql as pymysql
import json

LOG_FORMAT = "%(asctime)s - %(levelname)s - %(message)s"
DATE_FORMAT = "%Y/%m/%d %H:%M:%S %p"
logging.basicConfig(level=logging.DEBUG, format=LOG_FORMAT, datefmt=DATE_FORMAT)


def Generate_dbjson():
    connection = pymysql.connect(host='101.43.55.140', user='leqing', passwd='Ww0810083142', database='HDFSBUGDB',
                                 port=3306, charset='utf8')
    logging.info("Connect to 101.43.55.140:HDFSBUGDB Database success")
    with connection:
        # 获取IssueInfo、Type、Tag的映射
        with connection.cursor() as cursor:
            sql = "SELECT `IssueKey`,`id` FROM `IssueInfo` ORDER BY `id`"
            cursor.execute(sql)
            result = cursor.fetchall()
            Map = {}
            for r in result:
                Map.update({
                    r[0]: {
                        "Cause": "",
                        "Impact": "",
                        # Link：与之前哪个Issue有关
                        "Link": [],
                        "Classification": {
                            "Significance": "Vital",
                            "Quality": [],
                            "Component": [],
                            "Consequence": [],
                            "Code": []
                        },
                        "comment": ""
                    }
                }
                )
    with open(r"./new_db.json", 'w') as f:
        json.dump(Map, f)
    logging.info("Generate db最初副本.json success：./new_db.json")


def Check_db_json(file_path):
    with open(file_path) as f:
        db = dict(json.load(f))
        for k, v in db.items():
            Classification = v["Classification"]
            Significance = Classification["Significance"]

            if Significance not in ["Negligible", "Vital"]:
                raise Exception("检验失败1", k)

            if Significance == "Negligible":
                continue
            try:
                Impact = v["Impact"]
                Cause = v["Cause"]
            except Exception:
                raise Exception("检验失败2", k)

            if Impact == "" or Cause == "":
                raise Exception("检验失败3", k)

            for a, b in Classification.items():
                if a in ["Quality", "Component", "Consequence", "Code"]:
                    if b == "":
                        raise Exception("检验失败4", k, b)
        logging.info("数据检验成功")


def Update_dbjson(file_path):
    with open(file_path) as f:
        db = dict(json.load(f))
        for k, v in db.items():
            Classification = v["Classification"]
            map = {}
            for a, b in Classification.items():
                if a in ["Quality", "Component", "Consequence", "Code"]:
                    if Classification["Significance"] != "Vital":
                        map.update({
                            a: ""
                        })
                    else:
                        map.update({
                            a: b[0]
                        })
                else:
                    map.update({
                        a: b
                    })
            print(map)
            v.update({
                "Classification": map

            })
    print(db)
    with open(r"./update_db.json", 'w') as f:
        json.dump(db, f, ensure_ascii=False)
    logging.info("update db最初副本.json success：./update_db.json")


# def Transfer_db():
#     with open(r'res/db.json') as f:
#         db = dict(json.load(f))
#     with open(r"./new_db.json") as f2:
#         new_db = dict(json.load(f2))
#         for k, v in db.items():
#             new_db[k]["Cause"] = v["Cause"]
#             new_db[k]["Impact"] = v["Impact"]
#             new_db[k]["Link"] = v["Link"]
#             new_db[k]["comment"] = v["comment"]
#             new_db[k]["Classification"]["Quality"] = v["Tag"]["Quality"]
#             new_db[k]["Classification"]["Component"] = v["Tag"]["Component"]
#             new_db[k]["Classification"]["Code-Angle"].append(v["Type"]["Software"])
#     with open(r"./transfer_db.json", 'w') as f:
#         json.dump(new_db, f)

def Load_Label(file_path):
    connection = pymysql.connect(host='101.43.55.140', user='leqing', passwd='Ww0810083142', database='HDFSBUGDB',
                                 port=3306, charset='utf8')
    logging.info("Connect to 101.43.55.140:HDFSBUGDB Database success")
    with open(file_path) as f:
        Labels = dict(json.load(f))["Classification"]
        logging.info("Load Data：{}".format(Labels))

    # 为了让存入的数据id从1开始自增，首先清除数据
    with connection.cursor() as cursor:
        sqls = ["SET foreign_key_checks = 0", "TRUNCATE TABLE `Label`", "SET foreign_key_checks = 1"]
        for sql in sqls:
            cursor.execute(sql)
            connection.commit()
            logging.info(sql + " success")
    with connection.cursor() as cursor:
        sql = "INSERT INTO `Label` (Category,Name) VALUES (%s,%s);"
        for key, values in Labels.items():
            for v in values:
                cursor.execute(sql, (key, v))
        logging.info("Load Label success")
    logging.info("Start commit data to db")
    connection.commit()
    logging.info("Commit data success")


def Load_Research_Classify(file_path):
    connection = pymysql.connect(host='101.43.55.140', user='leqing', passwd='Ww0810083142', database='HDFSBUGDB',
                                 port=3306, charset='utf8')
    logging.info("Connect to 101.43.55.140:HDFSBUGDB Database success")
    Check_db_json(file_path)
    with open(file_path) as f:
        db = dict(json.load(f))
        # logging.info("Load Data：{}".format(db))
        db_len = len(db)
        logging.info("Len of Data:{}".format(db_len))

    with connection:
        # 获取IssueInfo、Label的映射
        with connection.cursor() as cursor:
            sqls = ["SELECT `IssueKey`,`id` FROM `IssueInfo` ORDER BY `id`",
                    "SELECT `Name`,`id` FROM `Label` ORDER BY `id`"]
            MAP = [{}, {}]
            for i in range(2):
                sql = sqls[i]
                map = MAP[i]
                cursor.execute(sql)
                result = cursor.fetchall()
                for t in result:
                    map[t[0]] = t[1]
        ISSUE_INFO = MAP[0]
        LABEL = MAP[1]
        logging.info("Obtaining the mapping succeeded：")
        logging.info("------------ISSUE_INFO(IssueKey,id):{}".format(ISSUE_INFO))
        logging.info("------------Label(NAME,id):{}".format(LABEL))
        # 为了让存入的数据id从1开始自增，首先清除数据
        with connection.cursor() as cursor:
            sqls = ["TRUNCATE TABLE Research", "TRUNCATE TABLE Classify"]
            for sql in sqls:
                cursor.execute(sql)
                logging.info(sql + " success")

        with tqdm(total=db_len) as pbar:
            pbar.set_description("Loadding")
            for IssueKey, data in db.items():
                pbar.update(1)
                IssueInfo_id = ISSUE_INFO[IssueKey]
                Cause = data['Cause']
                Impact = data['Impact']
                Link = '、'.join(list(data['Link']))
                Classification = dict(data['Classification'])
                Significance = Classification["Significance"]

                # 1. 写Research表
                with connection.cursor() as cursor:
                    if Significance == "Vital":
                        sql = "INSERT INTO `Research` (`IssueInfo_id`,`Cause`,`Impact`,`Link`) VALUES (%s,%s,%s,%s)"
                        cursor.execute(sql, (IssueInfo_id, Cause, Impact, Link))

                # 2. 写Classify表
                with connection.cursor() as cursor:
                    Quality = Classification["Quality"]
                    Component = Classification["Component"]
                    Consequence = Classification["Consequence"]
                    Code = Classification["Code"]
                    if Significance == "Vital":
                        try:
                            S = LABEL[Significance]
                            Q = LABEL[Quality]
                            Com = LABEL[Component]
                            Con = LABEL[Consequence]
                            Code = LABEL[Code]
                        except KeyError:
                            raise Exception("KeyError", IssueKey)
                    else:
                        S = LABEL[Significance]
                        Q = Com = Con = Code = None

                    sql = "INSERT INTO `Classify` (`IssueInfo_id`,`Significance`,`Quality`,`Component`,`Consequence`,`Code`) VALUES (%s,%s,%s,%s,%s,%s)"
                    cursor.execute(sql, (IssueInfo_id, S, Q, Com, Con, Code))

        logging.info("Start commit data to db")
        connection.commit()
        logging.info("Commit data success")
