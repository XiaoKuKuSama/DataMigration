select full_name,phone_no,agent_no,parent_agent_no,parent_agent_line,role_type,agent_level from agent_info where parent_agent_line like '-1987001-%';

select * from operator where agent_no='1987001'

select * from enterprise_info where agent_no='1987001'

select * from express_address where agent_no='1987001'









select * from agent_info where parent_agent_line like '1987001%';

select * from enterprise_info where agent_no='1987001';

select * from agent_info_ext;

select * from migration_info;

select * from sys_user; -- where agent_no='1987001';

select * from sys_role_user;

select username,count(0) from sys_role_user GROUP BY username;

delete from agent_info where parent_agent_line like '1987001%';
delete from enterprise_info where agent_no='1987001';
delete from agent_info_ext;
delete from migration_info;
DELETE from sys_role_user;
DELETE from sys_user;