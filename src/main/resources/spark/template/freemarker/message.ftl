<#if message??>
  <div id="message" class="${message.type}">${message.text}</div>
<#else>
  <div id="message" class="INFO" style="display:none">
    <!-- keep here for client-side messages -->
  </div>
</#if>
