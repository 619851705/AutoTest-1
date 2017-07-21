


create table at_global_setting(
setting_id int auto_increment primary key,
setting_name varchar(255),
default_value varchar(255),
setting_value varchar(255),
mark text
);

INSERT INTO `at_global_setting` (`setting_id`, `setting_name`, `default_value`, `setting_value`, `mark`) VALUES (1, 'home', '', NULL, '首页地址url');
INSERT INTO `at_global_setting` (`setting_id`, `setting_name`, `default_value`, `setting_value`, `mark`) VALUES (2, 'notice', '欢迎使用神州数码自动化测试平台！', NULL, '公告');
INSERT INTO `at_global_setting` (`setting_id`, `setting_name`, `default_value`, `setting_value`, `mark`) VALUES (3, 'version', '1.0.0', NULL, '系统版本');
INSERT INTO `at_global_setting` (`setting_id`, `setting_name`, `default_value`, `setting_value`, `mark`) VALUES (4, 'status', '0', NULL, '网站状态-0为开启,1为关闭');
INSERT INTO `at_global_setting` (`setting_id`, `setting_name`, `default_value`, `setting_value`, `mark`) VALUES (5, 'logSwitch', '0', NULL, 'log4j的日志开关');




create table at_role(
role_id int auto_increment primary key,
role_group varchar(120),
role_name varchar(120),
mark varchar(255)
);
INSERT INTO `at_role` (`role_id`, `role_group`, `role_name`, `mark`) VALUES (1, '性能测试组', 'admin', 'admin');
INSERT INTO `at_role` (`role_id`, `role_group`, `role_name`, `mark`) VALUES (3, '普通用户组', 'default', '默认组,不能删除');




create table at_user(
user_id int auto_increment primary key,
username varchar(120) unique not null,
real_name varchar(120) not null,
password varchar(120) not null,
login_identification varchar(255),
role_id int,
create_time datetime,
status char(1),
last_login_time datetime,
if_new char(1)
);
alter table at_user add constraint at_user_fk_role_id foreign key(role_id) REFERENCES at_role(role_id);
INSERT INTO `at_user` (`user_id`, `username`, `real_name`, `password`, `role_id`, `create_time`, `status`, `last_login_time`, `if_new`) VALUES (1, 'admin', '超级管理员', '6de56f9aad7db084b730f719d0874175', 1, '2016-09-18 14:36:06', '0', '2016-12-01 11:41:39', '1');



create table at_user_mail(
mail_id int auto_increment primary key,
receive_user_id int,
send_user_id int,
if_validate char(1),
mail_info longtext,
send_status char(1),
read_status char(1),
send_time datetime
);
alter table at_user_mail add constraint at_user_mail_fk_user_id_receive foreign key(receive_user_id) references at_user(user_id);
alter table at_user_mail add constraint at_user_mail_fk_user_id_send foreign key(send_user_id) references at_user(user_id);
	


create table at_operation_interface(
op_id int auto_increment primary key,
op_name varchar(255),
call_name varchar(255),
is_parent char(20),
op_type char(1),
mark varchar(255),
status char(1),
parent_op_id int
);	
alter table at_operation_interface add constraint at_operation_interface_fk_parent_op_id foreign key(parent_op_id) references at_operation_interface(op_id);		


create table at_role_power(
role_id int,
op_id int,
PRIMARY KEY  (role_id,op_id)
);		

alter table at_role_power add constraint at_role_power_fk_role_id foreign key(role_id) references at_role(role_id);
alter table at_role_power add constraint at_role_power_fk_op_id foreign key(op_id) references at_operation_interface(op_id) ON DELETE CASCADE;


create table at_data_db(
db_id int primary key,
db_type varchar(10),
db_url varchar(255),
db_name varchar(255),
db_username varchar(120),
db_passwd varchar(120),
db_mark varchar(255)
);



CREATE TABLE `at_task` (
	`task_id` INT(11) AUTO_INCREMENT,
	`task_name` varchar(255),
	`task_type` CHAR(1),
	`related_id` INT(11),
	`task_cron_expression` VARCHAR(100) ,
	`run_count` INT(11) NULL DEFAULT 0,
	`last_finish_time` DATETIME,
	`create_time` DATETIME,
	`status` CHAR(1),
	PRIMARY KEY (`task_id`)
);


create table at_interface_info(
interface_Id int auto_increment primary key,
interface_name varchar(120) unique not null,
interface_cn_name varchar(120),
request_url_mock text,
request_url_real text,
interface_type char(2),
interface_protocol varchar(50),
create_time datetime,
status char(1),
user_id int,
last_modify_user varchar(120)
);
alter table at_interface_info add constraint at_interface_fk_user_id foreign key(user_id) REFERENCES at_user(user_id);


create table at_parameter(
parameter_id int auto_increment primary key,
parameter_identify varchar(120),
parameter_name varchar(120),
default_value varchar(256),
path varchar(256),
type varchar(50),
interface_id int
);
alter table at_parameter add constraint at_parameter_fk_interface_id foreign key(interface_id) REFERENCES at_interface_info(interface_id);

INSERT INTO `at_parameter` (`parameter_id`, `parameter_identify`, `parameter_name`, `default_value`, `type`, `path`, `interface_id`) VALUES (1, '', '', '', 'Object', '', NULL);
INSERT INTO `at_parameter` (`parameter_id`, `parameter_identify`, `parameter_name`, `default_value`, `type`, `path`, `interface_id`) VALUES (2, '', '', '', 'Array_array', '', NULL);
INSERT INTO `at_parameter` (`parameter_id`, `parameter_identify`, `parameter_name`, `default_value`, `type`, `path`, `interface_id`) VALUES (3, '', '', '', 'Array_map', '', NULL);



create table at_complex_parameter(
id int auto_increment primary key,
self_parameter_id int,
next_complex_parameter_id int
);
alter table at_complex_parameter add constraint at_complex_parameter_fk_self_parameter_id foreign key(self_parameter_id) references at_parameter(parameter_id);
alter table at_complex_parameter add constraint at_complex_parameter_fk_next_complex_parameter_id foreign key(next_complex_parameter_id) references at_complex_parameter(id);



create table at_message(
message_id int auto_increment primary key,
message_name varchar(255),
message_type varchar(100),
interface_id int,
parameter_id int,
complex_parameter_id int,
parameter_json longtext,
call_parameter text,
request_url text,
user_id int,
create_time datetime,
status char(1),
last_modify_user varchar(120)
);
alter table at_message add constraint at_message_fk_interface_id foreign key(interface_id) references at_interface_info(interface_id);
alter table at_message add constraint at_message_fk_parameter_id foreign key(parameter_id) references at_parameter(parameter_id);
alter table at_message add constraint at_message_fk_user_id foreign key(user_id) references at_user(user_id);
alter table at_message add constraint at_message_fk_complex_parameter_id foreign key(complex_parameter_id) references at_complex_parameter(id);

create table at_message_scene(
message_scene_id int auto_increment primary key,
message_id int,
scene_name varchar(255),
validate_rule_flag char(1),
mark TEXT
);
alter table at_message_scene add constraint at_message_scene_pk_message_id foreign key(message_id) references at_message(message_id);


create table at_scene_validate_rule(
validate_id int auto_increment primary key,
message_scene_id int,
parameter_name varchar(255),
validate_value text,
get_value_method VARCHAR(20),
validate_method_flag char(1),
status char(1),
mark text
);

alter table at_scene_validate_rule add constraint at_scene_validate_rule_fk_message_scene_id foreign key(message_scene_id) references at_message_scene(message_scene_id);


create table at_test_data(
data_id int auto_increment primary key,
message_scene_id int,
params_data longtext,
status char(1)
);
alter table at_test_data add  data_discr varchar(256);
alter table at_test_data add constraint at_test_data_pk_message_scene_id foreign key(message_scene_id) references at_message_scene(message_scene_id);


create table at_test_set(
set_id int auto_increment primary key,
set_name varchar(255),
user_id int,
create_time datetime,
status char(1),
mark varchar(255)
);
alter table at_test_set add constraint at_test_set_fk_user_id foreign key(user_id) references at_user(user_id);


create table at_set_scene(
set_id int,
message_scene_id int,
PRIMARY KEY  (set_id,message_scene_id)
);
alter table at_set_scene add constraint at_set_scene_fk_set_id foreign key(set_id) references at_test_set(set_id);
alter table at_set_scene add constraint at_set_scene_fk_message_scene_id foreign key(message_scene_id) references at_message_scene(message_scene_id) ON DELETE CASCADE;


create table at_test_report(
report_id int auto_increment primary key,
test_mode varchar(10),
finish_flag char(1),
start_time datetime,
finish_time datetime,
user_id int
);
alter table at_test_report add constraint at_test_report_fk_user_id foreign key(user_id) references at_user(user_id);


create table at_test_result(
result_id int auto_increment primary key,
message_scene_id int,
report_id int,
message_info varchar(255),
request_url varchar(255),
request_message longtext,
response_message longtext,
use_time int,
run_status char(1),
status_code varchar(10),
op_time datetime,
protocol_type varchar(20),
mark longtext
);
alter table at_test_result add constraint at_test_result_fk_message_scene_id foreign key(message_scene_id) references at_message_scene(message_scene_id);
alter table at_test_result add constraint at_test_result_fk_report_id foreign key(report_id) references at_test_report(report_id);

create table at_test_config(
config_id int auto_increment primary key,
user_id int,
request_Url_Flag char(1),
connect_Time_Out int,
read_Time_Out int,
http_Method_Flag char(5),
validate_String varchar(255),
check_Data_flag char(1),
background_Exec_flag char(1)
);

insert into at_test_config values(null,0,'0',10000,5000,'0','0,0000,000000','0','1');


create table at_interface_mock(
mock_id int auto_increment primary key,
interface_name varchar(100) unique not null,
mock_url varchar(255),
request_json longtext,
response_json longtext,
create_time datetime,
user_id int,
if_validate char(1),
call_count int,
status char(1)
);

alter table at_interface_mock add constraint at_interface_mock_fk_user_id foreign key(user_id) references at_user(user_id);




create table at_web_object_category(
category_id int auto_increment primary key,
category_name varchar(120),
category_type varchar(50),
parent_category_id int
);

alter table at_web_object_category add constraint at_web_object_category_parent_category_id foreign key(parent_category_id) references at_web_object_category(category_id);
INSERT INTO `at_web_object_category` (`category_id`, `category_name`, `category_type`, `parent_category_id`) VALUES (1, 'Web自动化测试', 'all', NULL);


create table at_web_object(
object_id int auto_increment primary key,
object_name varchar(120),
object_type varchar(100),
how varchar(100),
object_seq int,
property_value varchar(255),
page_url text,
category_id int
);

alter table at_web_object add constraint at_web_object_category_id foreign key(category_id) references at_web_object_category(category_id);



create table at_web_case(
case_id int auto_increment primary key,
case_name varchar(120),
case_desc text,
browser varchar(50),
run_flag char(1),
user_id int,
create_time datetime
);
alter table at_web_case add constraint at_web_case_user_id foreign key(user_id) references at_user(user_id);


create table at_web_case_set(
set_id int auto_increment  primary key,
set_name varchar(100),
set_desc text,
test_count int,
user_id int,
create_time datetime,
last_modify_user varchar(100),
status char(1)
);
alter table at_web_case_set add constraint at_web_case_set_fk_user_id foreign key(user_id) references at_user(user_id);


create table at_web_case_set_comp(
id int auto_increment primary key,
set_id int,
case_id int,
status char(1),
user_id int,
submit_time datetime);
alter table at_web_case_set_comp add constraint at_web_case_set_comp_fk_set_id foreign key(set_id) references at_web_case_set(set_id);
alter table at_web_case_set_comp add constraint at_web_case_set_comp_fk_case_id foreign key(case_id) references at_web_case(case_id);
alter table at_web_case_set_comp add constraint at_web_case_set_comp_fk_user_id foreign key(user_id) references at_user(user_id);


create table at_web_step(
step_id int auto_increment primary key,
order_num int,
case_id int,
step_desc text,
step_method varchar(50),
object_id int,
call_method varchar(50),
require_parameter text,
require_value text,
require_parameter_type char(10),
capture char(1)
);
alter table at_web_step add constraint at_web_step_case_id foreign key(case_id) references at_web_case(case_id);
alter table at_web_step add constraint at_web_step_object_id foreign key(object_id) references at_web_object(object_id);


create table at_web_report_set(
report_set_id int auto_increment primary key,
set_id int,
test_time datetime
);
alter table at_web_report_set add constraint at_web_report_set_set_id foreign key(set_id) references at_web_case_set(set_id);

create table at_web_report_case(
report_case_id int auto_increment primary key,
case_id int,
report_set_id int,
test_time datetime
);
alter table at_web_report_case add constraint at_web_report_set_case_id foreign key(case_id) references at_web_case(case_id);
alter table at_web_report_case add constraint at_web_report_case_report_set_id foreign key(report_set_id) references at_web_report_set(report_set_id);

create table at_web_report(
report_id int auto_increment primary key,
step_id int,
report_case_id int,
run_status varchar(20),
test_mark text,
capture_path varchar(255),
test_user_name varchar(100),
op_time datetime
);
alter table at_web_report add constraint at_web_report_step_id foreign key(step_id) references at_web_step(step_id);
alter table at_web_report add constraint at_web_report_report_case_id foreign key(report_case_id) references at_web_report_case(report_case_id);


create table at_web_config(
config_id int auto_increment primary key,
user_id int,
element_wait_time int,
result_wait_time int,
ie_path varchar(255),
chrome_path varchar(255),
firefox_path varchar(255),
opera_path varchar(255),
window_size char(1),
error_interrupt_flag char(1)
);
INSERT INTO `at_web_config` (`config_id`, `user_id`, `element_wait_time`, `result_wait_time`, `ie_path`, `chrome_path`, `firefox_path`, `opera_path`, `window_size`, `error_interrupt_flag`) VALUES (1, 0, 5000, 3000, '', '', '', '', '1', '0');


create table at_web_step_category(
category_id int auto_increment primary key,
category_name varchar(255),
category_desc text,
create_user varchar(100),
submit_time datetime,
handle_time datetime,
category_tag varchar(255),
use_count int,
status char(1)
);		




create table at_web_public_step(
step_id int auto_increment primary key,
order_num int,
step_desc varchar(255),
step_method varchar(50),
object_id int,
call_method varchar(50),
require_parameter text,
require_value text,
require_parameter_type char(10),
capture char(1),
category_id int
);	
alter table at_web_public_step add constraint at_web_public_step_object_id foreign key(object_id) references at_web_object(object_id);
alter table at_web_public_step add constraint at_web_public_step_category_id foreign key(category_id)	references at_web_step_category(category_id);


create table at_web_test_rmi(
test_id int auto_increment primary key,
user_id int,
test_mode char(1),
related_id int,
submit_time datetime,
finish_time datetime,
status char(1),
test_msg text,
task_name varchar(255)
);
alter table at_web_test_rmi add constraint at_web_test_rmi_user_id foreign key(user_id) references at_user(user_id);


create table at_web_script_info(
script_id int auto_increment primary key,
script_name varchar(255),
script_path varchar(255),
if_public char(1),
script_desc text,
script_author varchar(80),
create_time datetime,
last_run_time datetime
);


create table at_web_script_report(
report_id int auto_increment primary key,
report_name varchar(255),
report_path varchar(255),
case_num int,
user_id int,
test_time datetime,
test_mark text
);

alter table at_web_script_report add constraint at_web_script_report_fk_user_id foreign key(user_id) references at_user(user_id);


INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (1, '自动化测试平台', '', 'true', '全局父节点', '0', 1);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (2, '接口自动化', NULL, 'true', '接口自动化', '0', 1);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (3, '接口管理', 'interface', 'true', '接口管理节点', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (4, '获取所有接口列表', 'interface-list', 'false', '查看当前处于正常状态的所有接口', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (5, '删除指定接口', 'interface-del', 'false', '删除指定id的接口信息,同样会删除下面关联的报文和场景等数据', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (6, '增加接口信息', 'interface-add', 'false', '增加一个新的接口信息', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (7, '查看指定接口的入参', 'interface-showParameters', 'false', '查看指定接口的所有入参列表', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (8, '删除接口的一个参数', 'interface-delParameter', 'false', '删除某个接口的一个参数', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (9, '编辑接口参数', 'interface-editParameter', 'false', '编辑指定接口的指定参数信息', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (10, '保存一个接口参数', 'interface-saveParameter', 'false', '给指定接口增加一个参数', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (11, '通过json导入指定接口的参数信息', 'interface-batchImportParams', 'false', '通过json字符串批量导入接口的参数', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (12, '查询指定接口信息', 'interface-find', 'false', '通过id查找指定接口的信息', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (13, '更新接口信息', 'interface-update', 'false', '编辑更新指定接口的详细信息', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (14, '根据条件查找接口', 'interface-filter', 'false', '根据传入的查询条Ian查找接口', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (15, '自动化测试', 'test', 'true', 'autoTes接口自动化测试节点', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (16, '单场景测试', 'test-sceneTest', 'false', '测试单个报文场景', '0', 15);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (17, '获取测试配置', 'test-getConfig', 'false', '获取指定用户的测试配置', '0', 15);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (18, '更新配置信息', 'test-updateConfig', 'false', '更新指定用户的配置信息', '0', 15);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (19, '检查测试集数据', 'test-validateData', 'false', '开始测试集测试之前检查数据', '0', 15);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (20, '后台执行测试', 'test-backgroundTest', 'false', '后台执行测试集测试', '0', 15);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (21, '非后台执行测试', 'test-commonTest', 'false', '非后台执行测试集测试', '0', 15);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (22, '报文管理', 'message', 'true', '报文管理节点', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (23, '获取报文列表', 'message-list', 'false', '获取报文列表', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (24, '删除报文', 'message-del', 'false', '删除指定报文', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (25, '格式化json串', 'message-format', 'false', '格式化报文json串', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (26, '验证报文入参合法性', 'message-validateJson', 'false', '验证报文入参合法性', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (27, '增加新报文', 'message-add', 'false', '增加新报文', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (28, '获取报文入参json串', 'message-getParamsJson', 'false', '获取报文入参json串', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (29, '获取指定报文信息', 'message-get', 'false', '获取指定报文信息', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (30, '更新指定报文信息', 'message-edit', 'false', '更新指定报文信息', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (31, '场景管理', 'messageScene', 'true', '场景管理节点', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (32, '获取场景列表', 'messageScene-list', 'false', '获取报文场景列表', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (33, '新增场景', 'messageScene-save', 'false', '新增新的报文场景', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (34, '删除场景', 'messageScene-del', 'false', '删除指定场景', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (35, '更新场景信息', 'messageScene-edit', 'false', '编辑更新场景信息', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (36, '获取指定场景信息', 'messageScene-get', 'false', '获取指定场景信息', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (37, '删除指定测试数据', 'messageScene-delData', 'false', '删除指定测试数据', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (38, '获取指定测试数据信息', 'messageScene-getData', 'false', '获取指定测试数据信息', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (39, '更新指定测试数据信息', 'messageScene-updateDataJson', 'false', '更新指定测试数据信息', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (40, '保存新的测试数据', 'messageScene-saveData', 'false', '保存新的测试数据', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (41, '接口mock管理', 'mock', 'true', '接口mock管理节点', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (42, '获取接口mock列表', 'mock-list', 'false', '获取接口mock列表', '0', 41);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (43, '获取指定接口mock信息', 'mock-get', 'false', '获取指定接口mock信息', '0', 41);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (44, '删除指定mock接口', 'mock-del', 'false', '删除指定mock接口', '0', 41);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (45, '编辑和增加接口mock信息', 'mock-edit', 'false', '编辑和增加接口mock信息', '0', 41);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (46, '测试报告管理', 'report', 'true', '测试报告管理节点', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (47, '测试集管理', 'set', 'true', '测试集管理节点', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (48, '获取测试报告列表', 'report-list', 'false', '获取测试报告列表', '0', 46);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (49, '查看测试详情列表', 'report-showResult', 'false', '查看测试详情列表', '0', 46);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (50, '查看指定场景的测试详情', 'report-showResultDetail', 'false', '查看指定场景的测试详情', '0', 46);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (51, '生成总览报告', 'report-htmlView', 'false', '生成总览报告', '0', 46);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (52, '删除测试报告', 'report-del', 'false', '删除测试报告', '0', 46);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (53, '获取测试集列表', 'set-list', 'false', '获取测试集列表', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (54, '删除测试集', 'set-del', 'false', '删除测试集', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (55, '新增测试集', 'set-save', 'false', '新增测试集', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (56, '获取指定测试集信息', 'set-find', 'false', '获取指定测试集信息', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (57, '更新测试集信息', 'set-edit', 'false', '更新测试集信息', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (58, '获取测试集下的测试场景', 'set-getScenes', 'false', '获取测试集下的测试场景', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (59, '删除测试集下的场景', 'set-delScene', 'false', '删除测试集下的场景', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (60, '获取测试集可以添加的测试场景', 'set-getNotScenes', 'false', '获取测试集可以添加的测试场景', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (61, '测试集增加场景', 'set-addScene', 'false', '测试集增加场景', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (62, '获取当前用户的测试集列表', 'set-getMy', 'false', '获取当前用户的测试集列表', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (63, '系统管理', NULL, 'true', '系统管理节点', '0', 1);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (64, '查询数据管理', 'db', 'true', '查询数据库管理节点', '0', 63);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (65, '获取查询数据库信息列表', 'db-list', 'false', '获取查询数据库信息列表', '0', 64);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (66, '删除查询数据库信息', 'db-del', 'false', '删除查询数据库信息', '0', 64);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (67, '更新查询数据库信息', 'db-edit', 'false', '更新查询数据库信息', '0', 64);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (68, '获取指定数据库信息', 'db-get', 'false', '获取指定数据库信息', '0', 64);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (69, '测试数据库连接', 'db-testDB', 'false', '测试数据库连接', '0', 64);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (70, '用户角色管理', NULL, 'true', '用户角色权限管理', '0', 1);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (71, '角色管理', 'role', 'true', '角色管理节点', '0', 70);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (72, '用户管理', 'user', 'true', '用户管理节点', '0', 70);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (73, '获取角色列表', 'role-list', 'false', '获取角色列表', '0', 71);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (74, '删除指定角色', 'role-del', 'false', '删除指定角色', '0', 71);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (75, '获取指定角色信息', 'role-get', 'false', '获取指定角色信息', '0', 71);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (76, '更新角色信息', 'role-edit', 'false', '更新角色信息', '0', 71);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (77, '获取角色权限信息', 'role-getNodes', 'false', '获取角色权限信息', '0', 71);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (78, '更新角色权限信息', 'role-updateRolePower', 'false', '更新角色权限信息', '0', 71);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (79, '获取当前用户列表', 'user-list', 'false', '获取当前用户列表', '0', 72);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (80, '删除指定用户', 'user-del', 'false', '删除指定用户', '0', 72);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (81, '锁定或解锁指定用户', 'user-lock', 'false', '锁定或解锁指定用户', '0', 72);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (82, '重置用户密码', 'user-resetPwd', 'false', '重置用户密码', '0', 72);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (83, '获取指定用户信息', 'user-get', 'false', '获取指定用户信息', '0', 72);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (84, '更新用户信息', 'user-edit', 'false', '更新用户信息', '0', 72);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (85, 'web自动化测试', NULL, 'true', 'web自动化测试节点', '0', 1);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (86, '测试用例管理', 'webCase', 'true', '测试用例管理节点', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (87, '测试用例集管理', 'caseSet', 'true', '测试用例集管理节点', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (88, '测试配置管理', 'webConfig', 'true', '测试配置管理节点', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (89, '测试对象管理', 'webObject', 'true', '测试对象管理节点', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (90, '测试报告管理', 'webReport', 'true', '测试报告管理节点', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (91, '测试公共步骤库管理', 'publicStep', 'true', '测试公共步骤库管理节点', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (92, 'web自动化测试', 'webTest', 'true', '自动化测试节点', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (93, '获取测试用例列表', 'webCase-list', 'false', '获取测试用例列表', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (94, '运行测试用例(服务端)', 'webCase-runTest', 'false', '运行测试用例(服务端)', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (95, '删除指定测试用例', 'webCase-delCase', 'false', '删除指定测试用例', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (96, '编辑更新测试用例基本信息', 'webCase-editCase', 'false', '编辑更新测试用例信息', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (97, '获取指定测试用例信息', 'webCase-getCase', 'false', '获取指定测试用例信息', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (98, '获取指定测试步骤信息', 'webCase-getStep', 'false', '获取指定测试步骤信息', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (99, '编辑指定测试步骤', 'webCase-editStep', 'false', '编辑指定测试步骤', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (100, '获取指定测试用例下的测试步骤', 'webCase-listStep', 'false', '获取指定测试用例下的测试步骤', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (101, '删除指定测试步骤', 'webCase-delStep', 'false', '删除指定测试步骤', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (102, '测试步骤排序', 'webCase-sortSteps', 'false', '测试步骤排序', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (103, '获取测试用例集列表', 'caseSet-list', 'false', '获取测试用例集列表', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (104, '增加测试用例到测试集', 'caseSet-addToSet', 'false', '增加测试用例到测试集', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (105, '删除测试集', 'caseSet-delSet', 'false', '删除测试集', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (106, '编辑更新测试集信息', 'caseSet-editSet', 'false', '编辑更新测试集信息', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (107, '获取当前用户的审核记录', 'caseSet-auditRecord', 'false', '获取当前用户的审核记录', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (108, '测试集测试(服务器端)', 'caseSet-batchTest', 'false', '测试集测试(服务器端)', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (109, '更新测试用例与测试集的关联状态(审核或者打回)', 'caseSet-updateCompStatus', 'false', '更新测试用例与测试集的关联状态(审核或者打回)', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (110, '获取指定测试用例集信息', 'caseSet-getSet', 'false', '获取指定测试用例集信息', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (111, '获取测试集下的测试用例列表', 'caseSet-getSetCase', 'false', '获取测试集下的测试用例列表', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (112, '获取当前用户测试配置表', 'webConfig-get', 'false', '获取当前用户测试配置表', '0', 88);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (113, '编辑当前用户测试配置表', 'webConfig-edit', 'false', '编辑当前用户测试配置表', '0', 88);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (114, '增加测试对象根节点', 'webObject-addCategory', 'false', '增加测试对象根节点', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (115, '修改测试对象节点关系', 'webObject-moveCategory', 'false', '修改测试对象节点关系', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (116, '获取指定节点下的测试对象或者全部对象', 'webObject-list', 'false', '获取指定节点下的测试对象或者全部对象', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (117, '增加或者修改测试对象', 'webObject-edit', 'false', '增加或者修改测试对象', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (118, '获取指定测试对象信息', 'webObject-get', 'false', '获取指定测试对象信息', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (119, '删除指定测试对象', 'webObject-del', 'false', '删除指定测试对象', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (120, '获取所有测试对象节点信息', 'webObject-getNodes', 'false', '获取所有测试对象节点信息', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (121, '删除指定节点', 'webObject-delCategory', 'false', '删除指定节点', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (122, '修改节点名称', 'webObject-updateCategoryName', 'false', '修改节点名称', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (123, '获取测试集报告列表', 'webReport-listReportSet', 'false', '获取测试集报告列表', '0', 90);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (124, '获取指定测试集报告信息', 'webReport-getReportSet', 'false', '获取指定测试集报告信息', '0', 90);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (125, '删除指定测试集报告', 'webReport-delReportSet', 'false', '删除指定测试集报告', '0', 90);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (126, '获取测试用例报告列表', 'webReport-listReportCase', 'false', '获取测试用例报告列表', '0', 90);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (127, '删除指定测试用例报告', 'webReport-delReportCase', 'false', '删除指定测试用例报告', '0', 90);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (128, '获取测试用例报告下的步骤报告列表', 'webReport-stepReport', 'false', '获取测试用例报告下的步骤报告列表', '0', 90);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (129, '获取公共步骤列表', 'publicStep-listCategory', 'false', '获取公共步骤列表', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (130, '复制指定公共步骤到测试用例', 'publicStep-copySteps', 'false', '复制指定公共步骤到测试用例', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (131, '推荐自己的测试步骤到公共步骤库', 'publicStep-addCategory', 'false', '推荐自己的测试步骤到公共步骤库', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (132, '获取当前用户的推荐历史', 'publicStep-auditRecord', 'false', '获取当前用户的推荐历史', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (133, '审核用户推荐', 'publicStep-updateCategoryStatus', 'false', '审核用户推荐(更改category状态)', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (134, '获取指定步骤库下的步骤列表', 'publicStep-listStep', 'false', '获取指定步骤库下的步骤列表', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (135, '获取指定公共测试步骤', 'publicStep-getStep', 'false', '获取指定公共测试步骤', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (136, '编辑指定公共测试步骤', 'publicStep-editStep', 'false', '编辑指定公共测试步骤', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (137, '删除指定公共步骤库', 'publicStep-delStepCategory', 'false', '删除指定测试公共步骤库', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (138, '更新公共步骤库备注信息', 'publicStep-editCategory', 'false', '更新公共步骤库备注信息', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (139, '获取当前公共步骤库待审核列表', 'publicStep-categoryAudit', 'false', '获取当前公共步骤库待审核列表', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (140, '获取当前测试任务列表(客户端)', 'webTest-taskList', 'false', '获取当前测试任务列表(客户端)', '0', 92);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (141, '发送测试请求到客户端(创建新的客户端测试任务)', 'webTest-runTest', 'false', '发送测试请求到客户端(创建新的客户端测试任务)', '0', 92);

