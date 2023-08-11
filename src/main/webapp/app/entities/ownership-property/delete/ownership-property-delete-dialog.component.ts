import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IOwnershipProperty } from '../ownership-property.model';
import { OwnershipPropertyService } from '../service/ownership-property.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './ownership-property-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OwnershipPropertyDeleteDialogComponent {
  ownershipProperty?: IOwnershipProperty;

  constructor(protected ownershipPropertyService: OwnershipPropertyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ownershipPropertyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
