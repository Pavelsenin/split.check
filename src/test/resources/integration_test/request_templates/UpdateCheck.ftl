{
  "name": "${CHECK_NAME}",
  "date": "${CHECK_DATE}",
  "users": [
    <#list CHECK_USERS_IDS as checkUserId>
    ${checkUserId}<#sep>,</#sep>
    </#list>
  ]
}