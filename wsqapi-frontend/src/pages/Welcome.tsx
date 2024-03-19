import React from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { Card, Alert, Typography } from 'antd';
import styles from './Welcome.less';
import { ProCard } from '@ant-design/pro-components';

const CodePreview: React.FC = ({ children }) => (
  <pre className={styles.pre}>
    <code>
      <Typography.Text copyable>{children}</Typography.Text>
    </code>
  </pre>
);

const Welcome: React.FC = () => {
  return (
    <PageContainer>
      <Card>
        <Alert
          message={'欢迎使用 WSQAPI 接口开放平台 🎉'}
          type="success"
          showIcon
          banner
          style={{
            margin: -12,
            marginBottom: 24,
          }}
        />
        <div>
          <Typography.Text strong>
            WSQAPI 接口开放平台是一个为用户和开发者提供丰富 API 接口调用服务的平台 🛠
          </Typography.Text>
        </div>
        <br/>
        <div>
          <Typography.Text strong>
            💻 作为『开发者』，可以在线选择所需接口并通过导入 <a
            href='https://github.com/Ryanwsq/wsqapi-client-sdk/tree/main/wsqapi-client-sdk'>WsqAPI-client-sdk</a> 快速在项目中集成调用接口的客户端，通过配置客户端的用户凭证快速调用接口，减轻开发成本，简化开发。
          </Typography.Text>
        </div>
        <br/>
        <div>
          <Typography.Text strong>
            😀 作为『用户』，可以查看接口列表，选择感兴趣的接口查看接口文档，在线调用接口，快速查看接口的返回值，判断接口的实现功能。
          </Typography.Text>
        </div>
        <br/>
        <div>
          <Typography.Text strong>
            🤝 作为『管理员』，可以管理接口和用户，管理接口时可以修改接口信息、上线、添加、发布和下线接口。管理用户时可以修改用户信息、禁用用户和解除用户的禁用等。
          </Typography.Text>
        </div>
        <br/>
        <div>
          <ProCard bordered gutter={16}>
            <ProCard title="多样化的接口选择" type="inner" bordered>
              WSQAPI 提供了丰富多样的接口用您选择，涵盖了开发中常用的功能，满足您的不同需求。
            </ProCard>
            <ProCard title="在线调试功能" type="inner" bordered>
              您可以在平台上进行接口在线调试，快速验证接口的功能和效果，节省了开发调试的时间和工作量。
            </ProCard>
            <ProCard title="开发者 SDK 支持" type="inner" bordered>
              为了方便开发者集成接口到自己的代码中，平台提供了客户端SDK，使调用接口变得更加简单和便捷。
            </ProCard>
          </ProCard>
        </div>
      </Card>
    </PageContainer>
  );
};

export default Welcome;
