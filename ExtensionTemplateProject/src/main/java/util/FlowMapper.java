// package util;

// import com.google.gson.Gson;
// import dto.FlowDTO;
// import dto.FlowEntryDTO;
// import manager.Flow;
// import manager.FlowEntry;
// import burp.api.montoya.MontoyaApi;

// import java.util.Base64;
// import java.util.List;
// import java.util.stream.Collectors;

// public class FlowMapper {
//     private static final Gson GSON = new Gson();

//     /** Convert model → DTO */
//     public static FlowDTO toDTO(Flow flow) {
//         List<FlowEntryDTO> edtos = flow.getEntries().stream()
//             .map(e -> {
//                 // raw bytes
//                 byte[] reqBytes  = e.getHttpRequestResponse().request().toByteArray();
//                 byte[] respBytes = (e.getHttpRequestResponse().response() != null)
//                     ? e.getHttpRequestResponse().response().toByteArray()
//                     : new byte[0];
//                 String r64  = Base64.getEncoder().encodeToString(reqBytes);
//                 String s64  = respBytes.length>0 
//                              ? Base64.getEncoder().encodeToString(respBytes)
//                              : null;
//                 return new FlowEntryDTO(
//                     e.notes(),    // preserve notes
//                     r64,
//                     s64
//                 );
//             })
//             .collect(Collectors.toList());
//         return new FlowDTO(flow.getFlowName(), flow.isActive(), edtos);
//     }

//     /** Convert DTO → model, rehydrating raw HTTP data */
//     public static Flow fromDTO(FlowDTO dto, MontoyaApi api) {
//         Flow flow = new Flow(dto.flowName);
//         flow.setActive(dto.isActive);

//         for (FlowEntryDTO ed : dto.entries) {
//             FlowEntry fe = new FlowEntry(
//                 ed.notes,
//                 ed.requestBase64,
//                 ed.responseBase64,
//                 api
//             );
//             flow.addEntry(fe);
//         }
//         return flow;
//     }

//     /** Helpers to serialize/deserialize a full map of flows */
//     public static String serializeAll(java.util.Map<String,Flow> flows) {
//         List<FlowDTO> dtos = flows.values().stream()
//             .map(FlowMapper::toDTO)
//             .collect(Collectors.toList());
//         return GSON.toJson(dtos);
//     }

//     public static java.util.List<FlowDTO> deserializeAll(String json) {
//         var listType = new com.google.gson.reflect.TypeToken<List<FlowDTO>>(){}.getType();
//         return GSON.fromJson(json, listType);
//     }
// }