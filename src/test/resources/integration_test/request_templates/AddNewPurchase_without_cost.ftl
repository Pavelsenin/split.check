{
  "name": "${GENERATED_PURCHASE_NAME}",
  "payer": "${PAYER_ID}",
  "consumers": [
    <#list CONSUMERS_IDS as consumerId>
    "${consumerId}"<#sep>,
    </#list>
  ]
}