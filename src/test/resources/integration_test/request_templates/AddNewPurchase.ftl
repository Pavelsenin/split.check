{
  "name": "${GENERATED_PURCHASE_NAME}",
  "cost": "${PURCHASE_COST}",
  "payer": "${PAYER_ID}",
  "consumers": [
    <#list CONSUMERS_IDS as consumerId>
    "${consumerId}"<#sep>,
    </#list>
  ]
}