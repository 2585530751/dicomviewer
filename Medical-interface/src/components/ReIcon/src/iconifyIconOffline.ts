import { h, defineComponent, type DefineComponent, type Component } from "vue";
import { Icon as IconifyIcon, addIcon } from "@iconify/vue/dist/offline";


// Iconify Icon在Vue里本地使用（用于内网环境）https://docs.iconify.design/icon-components/vue/offline.html
export default defineComponent({
  name: "IconifyIconOffline",
  components: { IconifyIcon },
  props: {
    icon: {
      default: null
    }
  },
  render() {
    if (typeof this.icon === "object"&&this.icon!=null) addIcon(this.icon, this.icon);
    const attrs = this.$attrs;
    return h(
      IconifyIcon as Component,
      {
        icon: this.icon,
        style: attrs?.style
          ? Object.assign(attrs.style, { outline: "none" })
          : { outline: "none" },
        ...attrs
      },
      {
        default: () => []
      }
    );
  }
});
