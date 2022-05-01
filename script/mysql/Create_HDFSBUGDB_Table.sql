CREATE DATABASE HDFSBUGDB;
use HDFSBUGDB;
create table IssueInfo
(
    id          int auto_increment
        primary key,
    Project     char(16) default 'Hadoop HDFS' not null comment '项目名',
    IssueType   char(8)                        not null comment 'Issue类型',
    IssueKey    char(16)                       not null comment 'IssueKey',
    Summary     text                           not null comment 'Issue概述',
    Priority    char(16)                       not null comment 'Issue优先级',
    Status      char(16)                       not null comment 'Issue状态',
    Resolution  char(16)                       not null comment 'Issue解决状态',
    CreatedTime char(64)                       not null comment 'Issue创建时间',
    UpdateTime  char(64)                       not null comment 'Issue最后更新时间',
    constraint IssueInfo_IssueKey_uindex
        unique (IssueKey),
    constraint IssueInfo_id_uindex
        unique (id)
);

create table Label
(
    id       int auto_increment
        primary key,
    Category char(12) not null comment 'Issue标签大类',
    Name     char(48) not null comment 'Issue标签小类',
    constraint Label_Name_uindex
        unique (Name),
    constraint Label_id_uindex
        unique (id)
);

create table Classify
(
    id           int auto_increment
        primary key,
    IssueInfo_id int not null comment 'IssueInfo的id',
    Significance int not null comment 'Label 重要性',
    Quality      int null comment 'Label 质量特性',
    Component    int null comment 'Label 组件',
    Consequence  int null comment 'Label 造成的结果',
    Code         int null comment 'Label 代码角度',
    constraint Classify_IssueInfo_id_uindex
        unique (IssueInfo_id),
    constraint Classify_id_uindex
        unique (id),
    constraint Classify_IssueInfo_fk
        foreign key (IssueInfo_id) references IssueInfo (id)
            on update cascade on delete cascade,
    constraint Classify_Label_Code_fk
        foreign key (Code) references Label (id)
            on update cascade on delete cascade,
    constraint Classify_Label_Component_fk
        foreign key (Component) references Label (id)
            on update cascade on delete cascade,
    constraint Classify_Label_Consequence_fk
        foreign key (Consequence) references Label (id)
            on update cascade on delete cascade,
    constraint Classify_Label_Quality_fk
        foreign key (Quality) references Label (id)
            on update cascade on delete cascade,
    constraint Classify_Label_Significance_fk
        foreign key (Significance) references Label (id)
            on update cascade on delete cascade
);

create table Research
(
    id           int auto_increment
        primary key,
    IssueInfo_id int  not null comment 'IssueInfo的id',
    Cause        text null comment 'Issue造成的原因',
    Impact       text null comment 'Issue的影响',
    Link         text null comment '相关联的其他issue',
    constraint Research_IssueInfo_id_uindex
        unique (IssueInfo_id),
    constraint Research_id_uindex
        unique (id),
    constraint Research_IssueInfo_fk
        foreign key (IssueInfo_id) references IssueInfo (id)
            on update cascade on delete cascade
);

