{
  "name": "${GENERATED_PURCHASE_NAME}",
  "cost": "${PURCHASE_COST}",
  "consumers": [
    <#list CONSUMERS_IDS as consumerId>
    "${consumerId}"<#sep>,
    </#list>
  ]
}