import {ProCard, ProDescriptions} from '@ant-design/pro-components';
import {
  getLoginUserUsingGETX,
  updateUserCredentials,
  updateUserUsingPOST,
} from '@/services/wsqapi-backend/userController';
import {values} from 'lodash';
import {Button, message} from 'antd';
import {useState} from 'react';

/**
 * 个人中心
 * @constructor
 */
const UserInfo: React.FC = () => {
  const [currentRow, setCurrentRow] = useState<API.UserVO>();
  const [reloadFlag, setReloadFlag] = useState(false);


  /**
   * @en-US Update certificate
   * @zh-CN 更新凭证
   * @param currentRow
   */
  const handleUpdateCertificate = async (currentRow: API.IdRequest) => {
    const hide = message.loading('正在更新');
    if (!currentRow) return true;
    try {
      await updateUserCredentials({
        id: currentRow.id,
      });
      hide();
      message.success('更新成功');
      // 点击按钮时更新状态变量的值
      setReloadFlag(prevFlag => !prevFlag);
      return true;
    } catch (error: any) {
      hide();
      message.error('更新失败，' + error.message);
      return false;
    }
  };

  return (
    <ProCard
      bordered
      headerBordered
      direction="column"
      gutter={[0, 16]}
      style={{ marginBlockStart: 8 }}
    >
      <ProCard title="个人信息" type="inner" bordered>
        <ProDescriptions
          column={1}
          request={async () => {
            const res = await getLoginUserUsingGETX({
              ...values(),
            });
            if (res?.data) {
              return {
                data: res?.data || '',
                success: true,
                total: res.data,
              };
            } else {
              return {
                data: '',
                success: false,
                total: 0,
              };
            }
          }}
          emptyText={'空'}
          editable={{
            // 编辑框修改后点击 ✔ 保存用户昵称
            onSave: async (keypath, newInfo, oriInfo) => {
              console.log(keypath, newInfo, oriInfo);
              const res = await updateUserUsingPOST(newInfo);
              console.log(res.data);
              if (res?.data) {
                return {
                  data: res?.data || '',
                  success: true,
                  total: res.data,
                };
              } else {
                return {
                  data: '',
                  success: false,
                  total: 0,
                };
              }
              return true;
            },
          }}
          columns={[
            {
              title: '用户名',
              dataIndex: 'userName',
              copyable: true,
            },
            {
              title: '角色',
              dataIndex: 'userRole',
              valueType: 'select',
              valueEnum: {
                user: { text: '普通用户', status: 'Default' },
                admin: { text: '管理员', status: 'Success' },
                suspend: { text: '禁用', status: 'Error' },
              },
              editable: false,
            },
            {
              title: '创建时间',
              dataIndex: 'createTime',
              valueType: 'dateTime',
              hideInForm: true,
              editable: false,
            },
          ]}
        ></ProDescriptions>
      </ProCard>
      <ProCard
        title="用户凭证"
        type="inner"
        bordered
        // 使用reloadFlag作为key来触发组件重新加载
        key={reloadFlag.toString()}
        extra={
          <Button
            onClick={() => {
              handleUpdateCertificate(currentRow);
            }}
          >
            更新凭证
          </Button>
        }
      >
        <ProDescriptions
          column={1}
          request={async () => {
            const res = await getLoginUserUsingGETX({
              ...values(),
            });
            setCurrentRow(res.data);
            if (res?.data) {
              return {
                data: res?.data || '',
                success: true,
                total: res.data,
              };
            } else {
              return {
                data: '',
                success: false,
                total: 0,
              };
            }
          }}
          emptyText={'空'}
          columns={[
            {
              title: 'AccessKey',
              dataIndex: 'accessKey',
              copyable: true,
            },
            {
              title: 'SecretKey',
              dataIndex: 'secretKey',
              copyable: true,
            },
          ]}
        ></ProDescriptions>
      </ProCard>

    </ProCard>
  );
};

export default UserInfo;
