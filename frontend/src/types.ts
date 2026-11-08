export type TransportMode = 'FLIGHT' | 'TRAIN' | 'BUS' | 'CAR';
export interface PlanRequest { budgetInr: number; sourceCity: string; days: number; transportMode: TransportMode; travelers: number; }
export interface UserProfile { id: string; name: string; email: string; role: string; }
export interface AuthResponse { token: string; tokenType: string; expiresInSeconds: number; user: UserProfile; }
export interface DestinationProfile { id: string; name: string; state: string; region: string; hubCity: string; rating: number; hotelBasePerNight: number; foodPerDay: number; activityBasePerDay: number; bestMonths: string[]; tags: string[]; idealDaysMin: number; idealDaysMax: number; guideText: string; }
export interface TransportOption { id: string; mode: TransportMode; providerName: string; sourceCity: string; destinationName: string; travelDate: string; departureWindow: string; price: number; durationHours: number; refundable: boolean; valueScore: number; }
export interface Hotel { id: string; destinationId: string; name: string; area: string; starTier: number; pricePerNight: number; rating: number; valueScore: number; breakfastIncluded: boolean; freeCancellation: boolean; }
export interface PartnerPackage { id: string; destinationId: string; name: string; partnerName: string; description: string; pricePerNight: number; tourBundlePrice: number; airportPickupValue: number; inclusions: string[]; }
export interface PartnerPackageQuote { partnerPackage: PartnerPackage; quoteTotal: number; estimatedSeparateCost: number; estimatedSavings: number; days: number; travelers: number; }
export interface ActivityItem { id: string; destinationId: string; name: string; description: string; price: number; durationHours: number; category: string; preferredSlot: string; }
export interface ItinerarySlot { timeLabel: string; activityName: string; description: string; cost: number; }
export interface DayPlan { dayNumber: number; title: string; slots: ItinerarySlot[]; estimatedActivityCost: number; }
export interface ItineraryPlan { destinationName: string; days: DayPlan[]; totalActivityCost: number; }
export interface AgentStep { agent: string; decision: string; detail: string; }
export interface ModeMinimum { transportMode: TransportMode; destinationName: string; minimumRequiredBudget: number; }
export interface DestinationMinimum { destinationId: string; destinationName: string; transportMode: TransportMode; transportCost: number; lodgingAndActivityCost: number; foodCost: number; subtotalBeforeBuffer: number; minimumRequiredBudget: number; partnerPackageAvailable: boolean; }
export interface TravelPlanResponse {
  feasible: boolean; request: PlanRequest; destination: DestinationProfile | null; transportOption: TransportOption | null; hotel: Hotel | null; partnerPackage: PartnerPackageQuote | null;
  activities: ActivityItem[]; itinerary: ItineraryPlan | null; costBreakdown: Record<string, number>; totalCost: number; remainingBudget: number; summary: string | null;
  guideHighlights: string[]; agentTrace: AgentStep[]; minimumRequiredBudget: number | null; reason: string | null; alternativeTransportModes: ModeMinimum[]; alternativeDestinations: DestinationMinimum[]; suggestions: string[];
}
