import {ProLayoutProps} from '@ant-design/pro-components';
/**
 * @name
 */
const Settings: ProLayoutProps & {
  pwa?: boolean;
  logo?: string;
  navTheme?:string
} = {
  navTheme: "light",
  layout: "top",
  contentWidth: "Fixed",
  fixedHeader: false,
  fixSiderbar: true,
  colorPrimary: "#1677FF",
  splitMenus: false,
  colorWeak: false,
  title: '接口开放平台',
  pwa: false,
  logo: '/logowsq.png',
  iconfontUrl: '',
};

export default Settings;

