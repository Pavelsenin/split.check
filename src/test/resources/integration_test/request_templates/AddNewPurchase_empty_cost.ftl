{
  "name": "${GENERATED_PURCHASE_NAME}",
  "cost": "",
  "payer": "${PAYER_ID}",
  "consumers": [
    <#list CONSUMERS_IDS as consumerId>
    "${consumerId}"<#sep>,
    </#list>
  ]
}